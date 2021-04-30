package com.example.myapplication.ui.todo

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_todo_item_detail.*

class TodoItemDetailActivity : AppCompatActivity() {
    //目前有显示了多少个提醒
    var noticeNum = 0

    companion object {
        fun actionStart(context: Context?) {

            val intent = Intent(context, TodoItemDetailActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_item_detail)
        initView()

    }

    fun initView() {
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

        todo_item_detail_add_ddl_constraintLayout.setOnClickListener {
            //For test
            todo_item_detail_add_ddl_text.text = "4月18日 周日 到期"
            todo_item_detail_add_ddl_del.visibility = View.VISIBLE
        }
        todo_item_detail_add_execute_constraintLayout.setOnClickListener {
            //For test
            todo_item_detail_add_execute_text.text = "4月18日 周日 xx:xx-xx:xx 执行"
            todo_item_detail_add_execute_del.visibility = View.VISIBLE
        }
        todo_item_detail_add_ddl_del.setOnClickListener {
            todo_item_detail_add_ddl_text.text = getString(R.string.todo_item_detail_add_ddl)
            todo_item_detail_add_ddl_del.visibility = View.GONE
        }
        todo_item_detail_add_execute_del.setOnClickListener {
            todo_item_detail_add_execute_text.text =
                getString(R.string.todo_item_detail_add_execute_time)
            todo_item_detail_add_execute_del.visibility = View.GONE
        }

        todo_item_detail_add_notice_Constraintlayout.setOnClickListener {
            //For test
            todo_item_detail_add_notice_Constraintlayout.visibility = View.GONE
            todo_item_detail_ddl_notice_ConstraintLayout.visibility = View.VISIBLE
            todo_item_detail_execute_notice_ConstraintLayout.visibility = View.VISIBLE
            noticeNum = 2
        }

        todo_item_detail_ddl_notice_del.setOnClickListener {
            todo_item_detail_ddl_notice_ConstraintLayout.visibility = View.GONE
            noticeNum -= 1
            todo_item_detail_add_notice_Constraintlayout.visibility = View.VISIBLE
        }

        todo_item_detail_execute_notice_del.setOnClickListener {
            todo_item_detail_execute_notice_ConstraintLayout.visibility = View.GONE
            noticeNum -= 1
            todo_item_detail_add_notice_Constraintlayout.visibility = View.VISIBLE
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


