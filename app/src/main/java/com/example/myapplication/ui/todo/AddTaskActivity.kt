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
import androidx.lifecycle.observe
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.Project
import com.example.myapplication.ui.todo.TodoViewModel
import com.example.myapplication.ui.todo.listItem
import com.example.myapplication.ui.todo.DaySelectDialogCreate
import com.example.myapplication.ui.todo.TimeBarDialogCreate
import com.example.myapplication.ui.uti.UtiFunc
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.ProjectsViewModelSimple
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.ProjectsViewModelSimpleFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*
import java.time.OffsetDateTime
import java.util.*
import kotlinx.android.synthetic.main.activity_setting_lists.*
import kotlinx.coroutines.flow.toList

@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity(), DaySelectDialogCreate.OnListener {
    private lateinit var addtaskViewModel: AddTaskViewModel
    private lateinit var projectsViewModelSimple: ProjectsViewModelSimple
    private lateinit var projectDao: ProjectDao
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

        projectDao=AppDatabase.getDatabase(this).projectDao()       //设置toolbar
        addTask_toolbar.title = "添加task"
        addTask_toolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(addTask_toolbar)
        addTask_toolbar.setNavigationOnClickListener { this.finish() }
        addtaskViewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)
        val currentProjectId=intent.getIntExtra("projectId",-1)
        val currentProjectName=intent.getStringExtra("projectName")

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
                    Log.d(TAG, "onCreate: "+addtaskViewModel.time_point2)
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
        projectsViewModelSimple=ViewModelProvider(this,ProjectsViewModelSimpleFactory(projectDao)).get(ProjectsViewModelSimple::class.java)
        projectsViewModelSimple.projectsListLiveData.observe(this, {
            val projectList=it as ArrayList<ProjectEntity>
            if(!projectList.isEmpty()){
                var choosedProject: ProjectEntity = projectList.get(0)
                if(currentProjectId!=-1) {
                    choosedProject= projectList.find{it?.project_id==currentProjectId }!!
                }
                collection.setText(choosedProject.project_name)
                var index=0
                if (!projectList.isEmpty()) {
                    for(aProject in projectList){
                        popupMenu2.menu.add(Menu.NONE, index, index, aProject?.project_name)
                        index+=1
                    }
                }
                popupMenu2.setOnMenuItemClickListener (fun(it: MenuItem): Boolean{
                    choosedProject=projectList.get(it.itemId)
                    collection.setText(choosedProject?.project_name)
                    return true
                })
            }
        })
        todo_project_type.setOnClickListener {
            popupMenu2.show()
        }

        val bt: FloatingActionButton = findViewById(R.id.button_submit)
        bt.setOnClickListener {
            //TODO 进行数据库插入操作


            this.finish()
        }
    }
}