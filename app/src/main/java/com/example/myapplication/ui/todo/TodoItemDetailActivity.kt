package com.example.myapplication.ui.todo

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.ui.uti.UtiFunc
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.TasksViewModelSimpleFactory
import kotlinx.android.synthetic.main.activity_todo_item_detail.*

class TodoItemDetailActivity : AppCompatActivity() {
    //目前有显示了多少个提醒
    var noticeNum = 0
    var id: Int = 0
    private lateinit var tasksViewModelSimple: TasksViewModelSimple

    companion object {
        fun actionStart(context: Context?, taskId: Int) {
            val intent = Intent(context, TodoItemDetailActivity::class.java)
            intent.putExtra("taskId", taskId)
            context?.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("taskId", -1)
        if (BuildConfig.DEBUG && id == -1) {
            error("Assertion failed")
        }
        setContentView(R.layout.activity_todo_item_detail)

        tasksViewModelSimple = ViewModelProvider(
            this,
            TasksViewModelSimpleFactory(AppDatabase.getDatabase(this).taskDao())
        ).get(TasksViewModelSimple::class.java)
        tasksViewModelSimple.getTaskLiveDataOfTaskId(id).observe(this, {
            initView(it)
        })



    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initView(taskItem:TaskEntity) {
        setSupportActionBar(todo_item_detail_toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        todo_item_detail_toolbar_down_pull.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.todo_item_detail_toobar_menu, popupMenu.menu)
            popupMenu.show()
        }
        todo_item_detail_toolbar_title.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.todo_item_detail_toobar_menu, popupMenu.menu)
            popupMenu.show()
        }

        todo_end_time_layout.setOnClickListener {
            val ddlTime=taskItem.todo_deadline
            if(ddlTime!=null){
                todo_item_detail_add_ddl_text.text = UtiFunc.Time2String(ddlTime)+" 到期"
                todo_item_detail_add_ddl_del.visibility = View.VISIBLE
            }
            else{
                todo_item_detail_add_ddl_del.visibility=View.GONE
            }
        }
        todo_execute_time_layout.setOnClickListener {
            val executeStartTime=taskItem.todo_execute_starttime
            val executeEndTime=taskItem.todo_execute_endtime
            if(executeStartTime!=null && executeEndTime!=null){
                collection.text = UtiFunc.Time2String(executeStartTime)+" 至 "+UtiFunc.Time2String(executeEndTime)+"执行"
                todo_item_detail_add_execute_del.visibility = View.VISIBLE
            }
            else
                todo_item_detail_add_execute_del.visibility=View.GONE

        }
        todo_item_detail_add_ddl_del.setOnClickListener {
            todo_item_detail_add_ddl_text.text = getString(R.string.todo_item_detail_add_ddl)
            todo_item_detail_add_ddl_del.visibility = View.GONE
        }
        todo_item_detail_add_execute_del.setOnClickListener {
            collection.text =
                getString(R.string.todo_item_detail_add_execute_time)
            todo_item_detail_add_execute_del.visibility = View.GONE
        }

        todo_remind_layout.setOnClickListener {
            //For test
            todo_remind_layout.visibility = View.GONE
            todo_item_detail_ddl_notice_ConstraintLayout.visibility = View.VISIBLE
            todo_item_detail_execute_notice_ConstraintLayout.visibility = View.VISIBLE
            noticeNum = 2
        }

        todo_item_detail_ddl_notice_del.setOnClickListener {
            todo_item_detail_ddl_notice_ConstraintLayout.visibility = View.GONE
            noticeNum -= 1
            todo_remind_layout.visibility = View.VISIBLE
        }

        todo_item_detail_execute_notice_del.setOnClickListener {
            todo_item_detail_execute_notice_ConstraintLayout.visibility = View.GONE
            noticeNum -= 1
            todo_remind_layout.visibility = View.VISIBLE
        }

        todo_item_detail_delete.setOnClickListener {
            //For test
            Toast.makeText(this, "已删除此事例", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}


