package com.example.myapplication.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.R
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.ui.todo.DaySelectDialogCreate
import com.example.myapplication.ui.todo.TimeBarDialogCreate
import com.example.myapplication.ui.uti.UtiFunc
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*
import java.time.OffsetDateTime
import java.util.*

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity(), DaySelectDialogCreate.OnListener {
    private lateinit var addtaskViewModel: AddTaskViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private var project_id=0
    private var important=false
    private var todo_execute_starttime: OffsetDateTime? = null
    private var todo_execute_endtime: OffsetDateTime? = null
    private var todo_execute_remind: OffsetDateTime? = null
    private var todo_deadline: OffsetDateTime? = null
    private var todo_deadline_remind: OffsetDateTime? = null
    private lateinit var alarmManager: AlarmManager
    private lateinit var pi:PendingIntent
    private val TAG = "AddTaskActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        //设置toolbar
        addTask_toolbar.title = "添加task"
        addTask_toolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(addTask_toolbar)
        addTask_toolbar.setNavigationOnClickListener { this.finish() }
        addtaskViewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)

        todo_end_time.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title", "设置截止时间")
            args.putInt("mode", 1)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "timeselect")
        }

        todo_execute_time.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title", "设置执行时间")
            args.putInt("mode", 2)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "timeselect")
        }

        val popupMenu1 = PopupMenu(
            this,
            todo_remind
        )
        //2、添加menu项目
        popupMenu1.menu.add(Menu.NONE, 0, 0, "截止时间提醒")
        popupMenu1.menu.add(Menu.NONE, 1, 1, "执行时间提醒")

        popupMenu1.setOnMenuItemClickListener(fun(it: MenuItem): Boolean {
            when (it.itemId) {
                0 -> {
                    val temp = Bundle()
                    temp.putInt("mode", 3)
                    val timedialog = TimeBarDialogCreate()
                    timedialog.arguments = temp
                    timedialog.show(supportFragmentManager, "ShowTimeBar")
                }
                1 -> {
                    val temp = Bundle()
                    temp.putInt("mode", 3)
                    val timedialog = TimeBarDialogCreate()
                    timedialog.arguments = temp
                    timedialog.show(supportFragmentManager, "ShowTimeBar")
                }
            }
            return true
        })
        todo_remind.setOnClickListener {
            popupMenu1.show()
        }

        //下面应该是根据数据库表来动态生成menu
        val popupMenu2 = PopupMenu(
            this,
            todo_project_type
        )
        popupMenu2.menu.add(Menu.NONE, 0, 0, "收件箱")
        popupMenu2.menu.add(Menu.NONE, 1, 1, "更多清单")
        popupMenu2.setOnMenuItemClickListener(fun(it: MenuItem): Boolean {
            when (it.itemId) {
                //这里之后填充点击的事件

            }
            return true
        })
        todo_project_type.setOnClickListener {
            popupMenu2.show()
        }

        todo_importance.setOnClickListener {
            important=!important
            if(important)
                todo_importance.setImageResource(R.drawable.ic_baseline_star_24)
            else
                todo_importance.setImageResource(R.drawable.ic_baseline_star_border_24)
        }

        val bt: FloatingActionButton = findViewById(R.id.button_submit)
        //确认按钮
        bt.setOnClickListener {
            var todo_priority=TaskPriority.COMMON
            if(important)
                todo_priority=TaskPriority.EMERGENCY
            mainViewModel.insertTask(
                OffsetDateTime.now(),
                TaskState.DOING,
                editTextTextMultiLine.text.toString(),
                1,
                todo_priority,
                todo_execute_starttime,
                todo_execute_endtime,
                todo_execute_remind,
                todo_deadline,
                todo_deadline_remind,
                text_description.text.toString()
            )
                    .observe(this, {})
            if(todo_deadline_remind!=null){
                AlarmService.addNotification(this,todo_deadline_remind.toString() , "deadline", editTextTextMultiLine.text.toString()+"任务还未完成哦！")
            }
            if(todo_execute_remind!=null){
                AlarmService.addNotification(this,todo_execute_remind.toString() , "execute", editTextTextMultiLine.text.toString()+"任务要做了哦！")
            }
            this.finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun confirm(flag: Boolean, mode: Int) {
        if(flag){
            if(mode==1) {
                todo_deadline = addtaskViewModel.time_point.value
                if (addtaskViewModel.time_point3.value != null)
                    todo_deadline_remind = addtaskViewModel.time_point3.value
                //提醒选择时间
                else if (addtaskViewModel.pre_time.value != null) {
                    todo_deadline_remind = when (addtaskViewModel.pre_time.value) {
                        "当天" -> {
                            todo_deadline
                        }
                        "提前1天" -> {
                            todo_deadline?.minusDays(1)
                        }
                        "提前2天" -> {
                            todo_deadline?.minusDays(2)
                        }
                        "提前3天" -> {
                            todo_deadline?.minusDays(3)
                        }
                        "提前1周" -> {
                            todo_deadline?.minusDays(7)
                        }
                        else -> {
                            null
                        }
                    }
                }
                if(todo_deadline!=null)
                    text__deadline.setText(UtiFunc.Time2String(todo_deadline!!))
            }
            else if(mode==2){
                //执行开始时间
                todo_execute_starttime= addtaskViewModel.time_point.value
                //执行结束时间
                todo_execute_endtime= addtaskViewModel.time_point2.value
                //执行提醒时间
                //执行自定义提醒时间
                if(addtaskViewModel.time_point3.value!=null)
                    todo_execute_remind=addtaskViewModel.time_point3.value
                //提醒选择时间
                else if(addtaskViewModel.pre_time2.value!=null){
                    todo_execute_remind=when(addtaskViewModel.pre_time2.value){
                        "当天" -> {
                            todo_execute_starttime
                        }
                        "提前1天" -> {
                            todo_execute_starttime?.minusDays(1)
                        }
                        "提前2天" -> {
                            todo_execute_starttime?.minusDays(2)
                        }
                        "提前3天" -> {
                            todo_execute_starttime?.minusDays(3)
                        }
                        "提前1周" -> {
                            todo_execute_starttime?.minusDays(7)
                        }
                        else -> {
                            null
                        }
                    }
                }
                if(todo_execute_starttime!=null)
                    text_execute_time.setText(UtiFunc.Time2String(todo_execute_starttime!!))
            }
            Log.d(TAG, "onCreate: pre_time " + addtaskViewModel.pre_time.value)
            Log.d(TAG, "onCreate: pre_time2 " + addtaskViewModel.pre_time2.value)
            Log.d(TAG, "onCreate: time_point " + addtaskViewModel.time_point.value)
            Log.d(TAG, "onCreate: time_point2" + addtaskViewModel.time_point2.value)
            Log.d(TAG, "onCreate: time_point3" + addtaskViewModel.time_point3.value)
        }
    }
}