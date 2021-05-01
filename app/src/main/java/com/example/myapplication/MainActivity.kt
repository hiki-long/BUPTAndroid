package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ui.fragment.SettingListsActivity
import com.example.myapplication.ui.todo.ListDialogCreate
import com.example.myapplication.ui.todo.TodoItem
import com.example.myapplication.ui.todo.TodoItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo_slide.*

@AndroidEntryPoint
class  MainActivity : AppCompatActivity() {
    private val todoItemList=ArrayList<TodoItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        initSlide()

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

    fun initSlide(){
        createData()
        val layoutManager=LinearLayoutManager(this)
        todo_slide_recyclerView.layoutManager=layoutManager
        val adapter=TodoItemAdapter(todoItemList)
        todo_slide_recyclerView.adapter=adapter

        todo_slide_add.setOnClickListener{
            addData()
            adapter.notifyDataSetChanged()
        }

        todo_slide_setting.setOnClickListener{
            var intent= Intent(this,SettingListsActivity::class.java)
            startActivity(intent)
        }
    }

    /*---------------测试函数---------------*/
    private fun createData(){
        var count:Int=1
        repeat(3){
            todoItemList.add(TodoItem("清单$count",10))
            count+=1
        }
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