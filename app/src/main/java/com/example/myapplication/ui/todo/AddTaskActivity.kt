package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.ui.todo.DaySelectDialogCreate
import com.example.myapplication.ui.todo.TimeBarDialogCreate
import com.example.myapplication.ui.todo.TodoViewModel
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_setting_lists.*
import kotlinx.coroutines.flow.toList

class AddTaskActivity : AppCompatActivity() {
    private lateinit var addtaskViewModel: AddTaskViewModel
    private lateinit var appDatabase:AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        appDatabase= Room.databaseBuilder(applicationContext,
            AppDatabase::class.java,"Database.db").allowMainThreadQueries().build()
        //设置toolbar
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
            args.putString("title","设置截止时间")
            args.putInt("mode",1)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "timeselect")
        }

        todo_execute_time.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title","设置执行时间")
            args.putInt("mode",2)
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
            when(it.itemId) {
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
        val projects=appDatabase.projectDao().getProjectsList()
        var choosedProject: ProjectEntity? = projects?.get(0)
        if(currentProjectId!=-1) {
            collection.setText(currentProjectName)
            choosedProject=projects?.find{it?.project_id==currentProjectId }
        }
        var index=0
        if (projects != null) {
            for(aProject in projects){
                    popupMenu2.menu.add(Menu.NONE, index, index, aProject?.project_name)
                    index+=1
            }
        }

        popupMenu2.setOnMenuItemClickListener (fun(it: MenuItem): Boolean{
            choosedProject=projects?.get(it.itemId)
            return true
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