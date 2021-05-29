package com.example.myapplication.ui.todo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.AddTaskViewModel
import com.example.myapplication.ui.uti.UtiFunc
import kotlinx.android.synthetic.main.todo_calendar.*
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class DaySelectDialogCreate : DialogFragment() {
    private var mode : Int? = null
    private var title: String? = null
    private val TAG = "DaySelectDialogCreate"
//    private lateinit var viewmodel: TodoViewModel
    private lateinit var viewmodel: AddTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null)
        {
            title = arguments?.getString("title")
            mode = arguments?.getInt("mode")
        }
//        viewmodel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        viewmodel = ViewModelProvider(requireActivity()).get(AddTaskViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view =  inflater.inflate(R.layout.todo_calendar, container, false) as View
        val dialogtitle = view.findViewById(R.id.time_dialog_title) as TextView
        val todotime = view.findViewById(R.id.select_todo_time) as Button
        val remindtime = view.findViewById(R.id.select_remind_time) as Button
        val cancel = view.findViewById(R.id.common_cancel) as Button
        val confirm = view.findViewById(R.id.confirm_alltime_button) as Button
        dialogtitle?.text =  title
        if(mode == 1)
        {
            todotime.text = "设置时间点"
            remindtime.text = "截止时间提醒"
            todotime.setOnClickListener {
                val temp = Bundle()
                temp.putInt("mode", 1)
                val timedialog = TimeBarDialogCreate()
                timedialog.arguments = temp
                timedialog.show(parentFragmentManager, "ShowTimeBar")
            }
            viewmodel.time_point.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    val Hour = String.format("%02d",it.hour)
                    val Minute = String.format("%02d",it.minute)
                    ChangeBarOne("设置截止时间($Hour:$Minute)")
                }
                else {
                    ChangeBarOne("设置截止时间")
                }
            })
            viewmodel.time_point3.observe(viewLifecycleOwner, {
                val time = UtiFunc.Time2String(it!!)
//                ChangeBarTwo("截止时间提醒(${it?.toLocalDate()} ${it?.hour}时${it?.minute}分)")
                ChangeBarTwo("截止时间提醒($time)")
            })
        }
        else
        {
            todotime.text = "设置时间段"
            remindtime.text = "执行时间提醒"
            todotime.setOnClickListener {
                val temp = Bundle()
                temp.putInt("mode", 2)
                val timedialog = TimeBarDialogCreate()
                timedialog.arguments = temp
                timedialog.show(parentFragmentManager, "ShowTimeBar")
            }
            viewmodel.time_point.observe(viewLifecycleOwner, Observer {
                val endtime = viewmodel.time_point2.value
                if (it != null) {
                    val BeginHour = String.format("%02d",it.hour)
                    val BeginMinute = String.format("%02d",it.minute)
                    val EndHour = String.format("%02d",endtime?.hour)
                    val EndMinute = String.format("%02d",endtime?.minute)
                    ChangeBarOne("设置执行时段($BeginHour:$BeginMinute-$EndHour:$EndMinute)")
                }
                else {
                    ChangeBarOne("设置执行时段")
                }
            })
            viewmodel.time_point2.observe(viewLifecycleOwner, Observer {
                val begintime = viewmodel.time_point.value
                if (it != null) {
                    val EndHour = String.format("%02d",it.hour)
                    val EndMinute = String.format("%02d",it.minute)
                    val BeginHour = String.format("%02d",begintime?.hour)
                    val BeginMinute = String.format("%02d",begintime?.minute)
                    ChangeBarOne("设置执行时段($BeginHour:$BeginMinute-$EndHour:$EndMinute)")
                }
                else {
                    ChangeBarOne("设置执行时段")
                }
            })
            viewmodel.time_point3.observe(viewLifecycleOwner, {
                val offset :Int = 9
                ChangeBarTwo("执行时间提醒(${it?.toLocalDate()} ${it?.hour}时${it?.minute}分)")
            })
        }

        cancel.setOnClickListener {
            dismiss()
        }

        confirm.setOnClickListener {
            onListener?.confirm(true,mode!!)
            dismiss()
        }

        remindtime.setOnClickListener {
            val temp = Bundle()
            temp.putInt("mode", mode!!)
            val optiondialog = TimeSelectDialogCreate()
            optiondialog.arguments = temp
            optiondialog.show(parentFragmentManager, "SelectOption")
            if(mode == 1) {
                viewmodel.pre_time.observe(viewLifecycleOwner, {
                    when(it!!.length) {
                        0 -> ChangeBarTwo("截止时间提醒")
                        else -> ChangeBarTwo("截止时间提醒($it)")
                    }
                })
            } else {
                viewmodel.pre_time2.observe(viewLifecycleOwner, {
                    when(it!!.length) {
                        0 -> ChangeBarTwo("执行时间提醒")
                        else -> ChangeBarTwo("执行时间提醒($it)")
                    }
                })
            }
        }
        return view
    }

    fun ChangeBarOne(title: String?){
        if(title != null)
        {
            val todotime = this.view?.findViewById(R.id.select_todo_time) as Button
            todotime.text = title
        }
    }

    fun ChangeBarTwo(title: String?){
        if(title != null)
        {
            val remindtime = view?.findViewById(R.id.select_remind_time) as Button
            remindtime.text = title
        }
    }

    private var onListener: OnListener? = null

    override fun onAttach(context:Context) {
        super.onAttach(context);
        if (context is OnListener) {
            onListener = context as OnListener;
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    override fun onDetach() {
        super.onDetach();
        onListener = null;
    }

    public interface OnListener{
        fun confirm(flag:Boolean,mode:Int)
    }


}