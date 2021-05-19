package com.example.myapplication.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.ui.todo.DaySelectDialogCreate
import com.example.myapplication.ui.todo.TimeBarDialogCreate
import com.example.myapplication.ui.todo.TodoViewModel
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_setting_lists.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var todoViewModel: TodoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
        //设置toolbar
        addTask_toolbar.title = "添加task"
        addTask_toolbar.setNavigationIcon(R.drawable.ic_back_24)
        setSupportActionBar(addTask_toolbar)
        addTask_toolbar.setNavigationOnClickListener { this.finish() }


//        todoViewModel =
//            ViewModelProvider(this).get(TodoViewModel::class.java)
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
        popupMenu2.menu.add(Menu.NONE, 0, 0, "收件箱")
        popupMenu2.menu.add(Menu.NONE, 1, 1, "更多清单")
        popupMenu2.setOnMenuItemClickListener (fun(it: MenuItem): Boolean{
            when(it.itemId){
                //这里之后填充点击的事件

            }
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