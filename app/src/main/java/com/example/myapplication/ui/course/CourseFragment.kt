package com.example.myapplication.ui.course

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.zhuangfei.timetable.TimetableView
import com.zhuangfei.timetable.listener.ISchedule
import com.zhuangfei.timetable.listener.ISchedule.OnSlideBuildListener
import com.zhuangfei.timetable.listener.ISchedule.OnWeekChangedListener
import com.zhuangfei.timetable.listener.IWeekView
import com.zhuangfei.timetable.listener.OnSlideBuildAdapter
import com.zhuangfei.timetable.model.Schedule
import com.zhuangfei.timetable.view.WeekView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CourseFragment : Fragment() {
    var mTimetableView: TimetableView? = null
    var mWeekView: WeekView? = null
    var moreButton: Button? = null
    var layout: LinearLayout? = null
    var titleTextView: TextView? = null
    var mySubjects: List<MySubject>? = SubjectRepertory.loadDefaultSubjects()

    private lateinit var courseViewModel: CourseViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        courseViewModel =
                ViewModelProvider(requireActivity()).get(CourseViewModel::class.java)
        courseViewModel.change.value = false
        val root = inflater.inflate(R.layout.fragment_course, container, false)
        titleTextView = root.findViewById(R.id.id_title);
        layout = root.findViewById(R.id.id_layout);
        layout?.setOnClickListener {
            if (mWeekView!!.isShowing) hideWeekView() else showWeekView()
        }
        moreButton = root.findViewById(R.id.id_more)
        moreButton!!.setOnClickListener { showPopmenu() }
        initTimetableView(root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).needDrawer(false)
        activity?.setTitle("??????")
        courseViewModel.change.observe(viewLifecycleOwner, Observer {
            updatecourse(it)
        })
        courseViewModel.deletedone.observe(viewLifecycleOwner, Observer {
            deletecourse(it)
        })
    }

    private fun initTimetableView(view: View) {
        //????????????
        mWeekView = view?.findViewById(R.id.id_weekview)
        mTimetableView = view?.findViewById(R.id.id_timetableView)

        //????????????????????????
        mWeekView!!.curWeek(1)
                .source(mySubjects)
                .callback { week ->
                    val cur: Int = mTimetableView!!.curWeek()
                    //???????????????????????????????????????cur->????????????week
                    mTimetableView!!.onDateBuildListener()
                        ?.onUpdateDate(cur!!, week)
                    mTimetableView!!.changeWeekOnly(week)
                }
                .callback(IWeekView.OnWeekLeftClickedListener {
                    onWeekLeftLayoutClicked()
                })
                .isShow(false) //???????????????????????????
                .showView()
        mTimetableView!!.curWeek(1)
                .source(mySubjects)
                .curTerm("???????????????")
                .callback(ISchedule.OnItemClickListener { v, scheduleList -> display(scheduleList) })
                .callback(ISchedule.OnItemLongClickListener { v, day, start ->
                    Toast.makeText(this.context,
                            "??????:???" + day + ",???" + start + "???",
                            Toast.LENGTH_SHORT).show()
                })
                .callback(OnWeekChangedListener { curWeek -> titleTextView?.setText("???" + curWeek + "???") })
                .showView()
    }

    override fun onStart() {
        super.onStart()
        mTimetableView!!.onDateBuildListener()
                .onHighLight();
        showWeekView()
    }

    protected fun onWeekLeftLayoutClicked() {
        val items = arrayOfNulls<String>(20)
        val itemCount = mWeekView!!.itemCount()
        for (i in 0 until itemCount) {
            items[i] = "???" + (i + 1) + "???"
        }
        var target: Int = -1
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        builder.setTitle("???????????????")
        builder.setSingleChoiceItems(items, mTimetableView!!.curWeek() - 1
        ) { _, i -> target = i }
        builder.setPositiveButton("??????????????????", DialogInterface.OnClickListener { dialog, which ->
            if (target !== -1) {
                mWeekView!!.curWeek(target + 1).updateView()
                mTimetableView!!.changeWeekForce(target + 1)
            }
        })
        builder.setNegativeButton("??????", null)
        builder.create().show()
    }

    fun showPopmenu() {
        val popup = PopupMenu(this.context, moreButton)
        popup.getMenuInflater().inflate(R.menu.popmenu_base_func, popup.getMenu())
        popup.setOnMenuItemClickListener{ it ->
                when (it.itemId) {
                    R.id.top1 -> addSubject()
                    R.id.top2 -> deleteSubject()
                    R.id.top3 -> hideNonThisWeek()
                    R.id.top4 -> showNonThisWeek()
                    R.id.top5 -> showTime()
                    R.id.top6 -> hideTime()
                    R.id.top7 -> hideWeekends()
                    R.id.top8 -> showWeekends()
                    else -> false
                }
            true
        }
        popup.show()
    }

    protected fun showTime() {
        val times = arrayOf(
            "8:00", "9:00", "10:10", "11:00",
            "15:00", "16:00", "17:00", "18:00",
            "19:30", "20:30", "21:30", "22:30"
        )
        val listener =
            mTimetableView!!.onSlideBuildListener() as OnSlideBuildAdapter
        listener.setTimes(times)
            .setTimeTextColor(Color.BLACK)
        mTimetableView!!.updateSlideView()
    }

    protected fun deleteSubject() {
        val data = mTimetableView!!.dataSource()
        val size = data.size
        if (size > 0) {
            val dialog = DeleteCourseDialog()
            val temp = Bundle()
            val passlist: ArrayList<String> = arrayListOf()
            for(item in data) {
                passlist.add(item.name)
            }
            temp.putStringArrayList("data", passlist)
            dialog.arguments = temp
            dialog.show(parentFragmentManager, "deletedialog")

        } else {
            Toast.makeText(this.context, "??????????????????", Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * ????????????
     * ??????????????????????????????????????????????????????????????????????????????
     * ??????????????????????????????????????????
     */

    protected fun addSubject() {
        val dialog = AddCourseDialog()
        dialog.show(this.parentFragmentManager, "Course dialog")
    }

    /**
     * ????????????
     * ?????????????????????Null???????????????????????????????????????????????????????????????
     * ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    protected fun hideTime() {
        mTimetableView!!.callback(null as OnSlideBuildListener?)
        mTimetableView!!.updateSlideView()
    }

    /**
     * ?????????????????????
     * ?????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????
     *
     *
     * updateView()???????????????????????????????????????????????????????????????
     */
    protected fun hideNonThisWeek() {
        mTimetableView!!.isShowNotCurWeek(false).updateView()
    }

    /**
     * ?????????????????????
     * ?????????????????????????????????????????????????????????????????????
     * ???????????????????????????????????????
     */
    protected fun showNonThisWeek() {
        mTimetableView!!.isShowNotCurWeek(true).updateView()
    }

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     */
    fun hideWeekView() {
        mWeekView!!.isShow(false)
        titleTextView!!.setTextColor(resources.getColor(R.color.app_course_textcolor_blue))
        val cur = mTimetableView!!.curWeek()
        mTimetableView!!.onDateBuildListener()
                .onUpdateDate(cur, cur)
        mTimetableView!!.changeWeekOnly(cur)
    }

    fun showWeekView() {
        mWeekView!!.isShow(true)
        titleTextView!!.setTextColor(resources.getColor(R.color.app_red))
    }

    /**
     * ????????????
     */
    private fun hideWeekends() {
        mTimetableView!!.isShowWeekends(false).updateView()
    }

    /**
     * ????????????
     */
    private fun showWeekends() {
        mTimetableView!!.isShowWeekends(true).updateView()
    }

    private fun deletecourse(isdelete: Boolean) {
        if(isdelete) {
            val dataSource = mTimetableView!!.dataSource()
            val tempnamelist: MutableSet<String> = mutableSetOf()
            for(index in courseViewModel.deletelist.value!!) {
                tempnamelist.add(dataSource[index].name)
            }
            dataSource.removeIf { tempnamelist.contains(it.name) }
            mTimetableView!!.updateView()
            courseViewModel.deletedone.value = false
            courseViewModel.deletelist.value = mutableSetOf()
        }
    }

    private fun updatecourse(ischange: Boolean) {
        if(ischange) {
            val dataSource = mTimetableView!!.dataSource()
            val size = dataSource!!.size
            if (size > 0) {
                val schedule = Schedule()
                schedule.colorRandom = 2
                schedule.name = courseViewModel.name.value
                schedule.day = courseViewModel.day.value!!
                schedule.room = courseViewModel.room.value
                schedule.start = courseViewModel.start.value!!
                schedule.step = courseViewModel.step.value!!
                schedule.teacher = courseViewModel.teacher.value
                schedule.weekList = courseViewModel.weekList.value
                dataSource.add(schedule)
                mTimetableView!!.updateView()
            }
            courseViewModel.change.value = false
        }
    }

    protected fun display(beans: List<Schedule>) {
        val bean = beans[0]
        val temp = Bundle()
        temp.putString("name", bean.name)
        var week:String = ""
        when(bean.day) {
            1 -> week = "???"
            2 -> week = "???"
            3 -> week = "???"
            4 -> week = "???"
            5 -> week = "???"
            6 -> week = "???"
            7 -> week = "???"
        }
        temp.putString("week", week)
        temp.putInt("start", bean.start)
        temp.putInt("end", bean.start+bean.step)
        temp.putString("teacher", bean.teacher)
        temp.putString("room", bean.room)
        temp.putInt("weekstart", bean.weekList[0])
        temp.putInt("weekend", bean.weekList.takeLast(1)[0])
        val dialog = ShowCourseDialog()
        dialog.arguments = temp
        dialog.show(parentFragmentManager, "dialog")
    }
}