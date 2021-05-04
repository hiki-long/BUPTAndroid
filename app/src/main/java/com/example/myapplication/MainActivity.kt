package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ui.fragment.SettingListsActivity
import com.example.myapplication.ui.todo.ListDialogCreate
import com.example.myapplication.ui.todo.TodoItem
import com.example.myapplication.ui.todo.TodoItemAdapter
import com.example.myapplication.ui.todo.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_lists.*
import kotlinx.android.synthetic.main.fragment_todo_slide.*
import kotlinx.android.synthetic.main.todo_item_list_item_view.view.*

@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {
    private val todoItemList=ArrayList<TodoItem>()
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var adapter:TodoItemAdapter;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter=initSlide()
        todoViewModel =
            ViewModelProvider(this).get(TodoViewModel::class.java)

        todoViewModel.lists.observe( this,  {
            todoItemList.clear()
            for (value in it)
            {
                if (value != null) {
                    todoItemList.add(TodoItem(value.project_name,value.tasks.size))
                }
                adapter.notifyDataSetChanged()
            }
        }
        )
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_todo, R.id.navigation_view, R.id.navigation_course, R.id.navigation_more))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setSupportActionBar(toolbar)

        //tobar drawer open button
        toolbar.setNavigationOnClickListener{
            if(navView.selectedItemId==R.id.navigation_todo ) {
                if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mainDrawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mainDrawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }

        val toggle =object: ActionBarDrawerToggle(this,mainDrawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                navView.menu.forEach { it.isEnabled=true }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                navView.menu.forEach { it.isEnabled=false }
            }
        }
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    //依附于MainActivity的fragment需要在onViewCreated调用needDrawer函数决定是否打开drawer。
    fun needDrawer(judge: Boolean){
        if(judge){
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        else{
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }

    fun setTopListVisibility(){
        var shp=getSharedPreferences("list_settings", Context.MODE_PRIVATE)
        if(shp.getBoolean("autohind",false)){
            todo_slide_all.visibility = if (todo_slide_all.todo_item_list_item_view_num.text!="0") View.VISIBLE else View.GONE
            todo_slide_planned.visibility =
                if (todo_slide_planned.todo_item_list_item_view_num.text!="0") View.VISIBLE else View.GONE
            todo_slide_important.visibility =
                if (todo_slide_important.todo_item_list_item_view_num.text!="0") View.VISIBLE else View.GONE
            todo_slide_finished.visibility =
                if (todo_slide_finished.todo_item_list_item_view_num.text!="0") View .VISIBLE else View.GONE
            todo_slide_today.visibility =
                if (todo_slide_today.todo_item_list_item_view_num.text!="0") View.VISIBLE else View.GONE
        }
        else {
            todo_slide_all.visibility = if (shp.getBoolean("all", true)) View.VISIBLE else View.GONE
            todo_slide_planned.visibility =
                if (shp.getBoolean("planned", true)) View.VISIBLE else View.GONE
            todo_slide_important.visibility =
                if (shp.getBoolean("important", true)) View.VISIBLE else View.GONE
            todo_slide_finished.visibility =
                if (shp.getBoolean("completed", true)) View.VISIBLE else View.GONE
            todo_slide_today.visibility =
                if (shp.getBoolean("today", true)) View.VISIBLE else View.GONE
        }
    }

    fun initSlide():TodoItemAdapter{
        var shpEdit=getSharedPreferences("list_settings", Context.MODE_PRIVATE).edit()
        shpEdit.putBoolean("all", true)
        shpEdit.putBoolean("planned", true)
        shpEdit.putBoolean("important", true)
        shpEdit.putBoolean("completed", true)
        shpEdit.putBoolean("today", true)
        shpEdit.putBoolean("autohind",false)
        shpEdit.apply()

        setTopListVisibility()
        createData()
        val layoutManager=LinearLayoutManager(this)
        todo_slide_recyclerView.layoutManager=layoutManager
        val adapter=TodoItemAdapter(todoItemList)
        todo_slide_recyclerView.adapter=adapter

        todo_slide_add.setOnClickListener{
            addData()
        }

        todo_slide_setting.setOnClickListener{
            var intent= Intent(this,SettingListsActivity::class.java)
            startActivity(intent)
        }
        return adapter
    }

    override fun onResume() {
        super.onResume()
        setTopListVisibility()
    }


    private fun createData(){

    }

    private fun addData(){
//        todoItemList.add(TodoItem("新增的清单",0))
        var dialog = ListDialogCreate()
        //这里的bundle是用来传输标题的数据,小型的数据都可以用bundle传
        var args = Bundle()
        args.putString("title", "添加清单")
        dialog.arguments = args
        dialog.show(supportFragmentManager, "listdialog")
    }
}