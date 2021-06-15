package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.ui.fragment.SettingListsActivity
import com.example.myapplication.ui.todo.*
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.ProjectsViewModelSimple
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.ProjectsViewModelSimpleFactory
import com.example.myapplication.viewmodelFactory.TasksViewModelSimpleFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo_slide.*
import kotlinx.android.synthetic.main.todo_item_list_item_view.view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val listList = ArrayList<listItem>()
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: TodoItemAdapter
    private lateinit var tasksViewModelSimple: TasksViewModelSimple
    private lateinit var projectsViewModelSimple:ProjectsViewModelSimple
    private val tasksOfAProjectLiveDataList=ArrayList<LiveData<List<TaskEntity>>>()
    lateinit private var exclusiveSlideButtonList:Array<View>
    lateinit var todoFragment:TodoFragment
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDao = AppDatabase.getDatabase(this).taskDao()
        tasksViewModelSimple = ViewModelProvider(
            this,
            TasksViewModelSimpleFactory(taskDao)
        ).get(TasksViewModelSimple::class.java)
        projectsViewModelSimple=ViewModelProvider(
            this,
            ProjectsViewModelSimpleFactory(AppDatabase.getDatabase(this).projectDao()))
            .get(ProjectsViewModelSimple::class.java)
        adapter = initSlide()


        exclusiveSlideButtonList=arrayOf(todo_slide_all,todo_slide_today,todo_slide_planned,
            todo_slide_important,todo_slide_finished,todo_slide_collectbox)
        //设置all选中
        todo_slide_all.setBackgroundColor(Color.argb(66 ,3,169,244))

        projectsViewModelSimple.projectsListLiveData.observe(this,{
            var hasdefault=false
            if(it.size!=0){
                val default= it[0]
                if(default!=null){
                    if(default.project_name!="收集箱"){
                        projectsViewModelSimple.updateAProject(1,"收集箱",0)
                    }
                    hasdefault=true;
                }
            }
            if(hasdefault==false){
                projectsViewModelSimple.insertAProject("收集箱",0)
            }
        })
        projectsViewModelSimple.projectsListLiveData.observe(this, {
            tasksOfAProjectLiveDataList.forEach {
                it.removeObservers(this)
            }
            listList.clear()
            tasksOfAProjectLiveDataList.clear()
            for (value in it) {
                if (value != null) {
                    if(value.project_id==1){
                        continue
                    }
                    listList.add(listItem(value.project_name, 0, value.project_id))
                }
            }
            for(index in 0 until  listList.size){
                val item=listList[index]
                val id=item.projectId
                val liveData=tasksViewModelSimple.getTasksLiveDataOfAProject(id)
                liveData.observe(
                    this,
                    {
                        listList[index].todoNum=it.size
                        adapter.notifyDataSetChanged()
                    }
                )
                tasksOfAProjectLiveDataList.add(liveData)
            }
        }

        )
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_todo,
                R.id.navigation_view,
                R.id.navigation_course,
                R.id.navigation_more
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setSupportActionBar(toolbar)

        //tobar drawer open button
        toolbar.setNavigationOnClickListener {
            if (navView.selectedItemId == R.id.navigation_todo) {
                if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mainDrawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mainDrawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }

        val toggle = object : ActionBarDrawerToggle(
            this,
            mainDrawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                navView.menu.forEach { it.isEnabled = true }
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                navView.menu.forEach { it.isEnabled = false }
            }
        }
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    //依附于MainActivity的fragment需要在onViewCreated调用needDrawer函数决定是否打开drawer。
    fun needDrawer(judge: Boolean) {
        if (judge) {
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }

    fun setTopListVisibility() {
        var shp = getSharedPreferences("list_settings", Context.MODE_PRIVATE)
        if (shp.getBoolean("autohind", false)) {
            todo_slide_all.visibility =
                if (todo_slide_all.todo_item_list_item_view_num.text != "0") View.VISIBLE else View.GONE
            todo_slide_planned.visibility =
                if (todo_slide_planned.todo_item_list_item_view_num.text != "0") View.VISIBLE else View.GONE
            todo_slide_important.visibility =
                if (todo_slide_important.todo_item_list_item_view_num.text != "0") View.VISIBLE else View.GONE
            todo_slide_finished.visibility =
                if (todo_slide_finished.todo_item_list_item_view_num.text != "0") View.VISIBLE else View.GONE
            todo_slide_today.visibility =
                if (todo_slide_today.todo_item_list_item_view_num.text != "0") View.VISIBLE else View.GONE
            todo_slide_collectbox.visibility=
                if (todo_slide_collectbox.todo_item_list_item_view_num.text!="0") View.VISIBLE else View.GONE
        } else {
            todo_slide_all.visibility = if (shp.getBoolean("all", true)) View.VISIBLE else View.GONE
            todo_slide_planned.visibility =
                if (shp.getBoolean("planned", true)) View.VISIBLE else View.GONE
            todo_slide_important.visibility =
                if (shp.getBoolean("important", true)) View.VISIBLE else View.GONE
            todo_slide_finished.visibility =
                if (shp.getBoolean("completed", true)) View.VISIBLE else View.GONE
            todo_slide_today.visibility =
                if (shp.getBoolean("today", true)) View.VISIBLE else View.GONE
            todo_slide_collectbox.visibility=
                if (shp.getBoolean("collectbox", true)) View.VISIBLE else View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initSlide(): TodoItemAdapter {

        setTopListVisibility()
        createData()
        val layoutManager = LinearLayoutManager(this)
        todo_slide_recyclerView.layoutManager = layoutManager
        todoFragment =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).childFragmentManager.fragments.get(
                0
            ) as TodoFragment
        val adapter = TodoItemAdapter(listList, todoFragment)
        todo_slide_recyclerView.adapter = adapter

        setTopTasksNum()

        todo_slide_all.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_all.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterAll)
        }
        todo_slide_planned.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_planned.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterPlanned)
        }
        todo_slide_important.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_important.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterImportant)
        }
        todo_slide_finished.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_finished.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterFinished)
        }
        todo_slide_today.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_today.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterToday)
        }
        todo_slide_collectbox.setOnClickListener {
            mainDrawerLayout.closeDrawer(GravityCompat.START)
            slideTopCancelSelected()
            todo_slide_collectbox.setBackgroundColor(Color.argb(66 ,3,169,244))
            todoFragment.databaseBinder(TodoListDisplayOptions.filterCollectbox)
        }
        todo_slide_add.setOnClickListener {
            addData()
        }

        todo_slide_setting.setOnClickListener {
            var intent = Intent(this, SettingListsActivity::class.java)
            startActivity(intent)
        }


        return adapter
    }

    override fun onResume() {
        super.onResume()
        setTopListVisibility()
    }


    private fun createData() {

    }

    private fun addData() {
//        todoItemList.add(TodoItem("新增的清单",0))
        var dialog = ListDialogCreate()
        //这里的bundle是用来传输标题的数据,小型的数据都可以用bundle传
        var args = Bundle()
        args.putString("title", "添加清单")
        dialog.arguments = args
        dialog.show(supportFragmentManager, "listdialog")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTopTasksNum(){
        tasksViewModelSimple.tasksLiveData.observe(this, {
            todo_slide_all.updateTodoNum(it.size)
            setTopListVisibility()
        })
        tasksViewModelSimple.todayTasksLiveData().observe(this,{
            todo_slide_today.updateTodoNum(it.size)
            setTopListVisibility()
        })
        tasksViewModelSimple.importantTasksLiveData.observe(this,{
            todo_slide_important.updateTodoNum(it.size)
            setTopListVisibility()
        })
        tasksViewModelSimple.plannedTasksLiveData.observe(this,{
            todo_slide_planned.updateTodoNum(it.size)
            setTopListVisibility()
        })
        tasksViewModelSimple.finishedTasksLiveData.observe(this,{
            todo_slide_finished.updateTodoNum(it.size)
            setTopListVisibility()
        })
        tasksViewModelSimple.collecboxTaksLiveData.observe(this,{
            todo_slide_collectbox.updateTodoNum(it.size)
            setTopListVisibility()
        })
    }

    fun replaceAdapterFragment(fragment: TodoFragment){
        if(this::adapter.isInitialized){
            adapter.updateFragementInstance(fragment)
            todoFragment=fragment
        }
    }

    fun slideTopCancelSelected()=exclusiveSlideButtonList.forEach { it.setBackgroundColor(Color.rgb(255,255,255)) }
}