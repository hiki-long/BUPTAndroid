package com.example.myapplication.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.model.Project
import com.example.myapplication.ui.adapter.ProjectAdapter
import com.example.myapplication.ui.todo.ListDialogCreate
import com.example.myapplication.viewmodel.ProjectsViewModelSimple
import com.example.myapplication.viewmodelFactory.ProjectsViewModelSimpleFactory
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_lists.*

@AndroidEntryPoint
class SettingListsActivity : AppCompatActivity() {
    private var projectList=ArrayList<ProjectEntity>()
//    private lateinit var liveProjectList:LiveData<ArrayList<Project>>
    private lateinit var projectsViewModelSimple: ProjectsViewModelSimple
    lateinit var adapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_lists)
        //设置toolbar
        setSupportActionBar(setting_toolbar)
        setting_toolbar.title = "清单设置"
        setting_toolbar.setNavigationIcon(R.drawable.ic_back_24)
        setting_toolbar.setNavigationOnClickListener { this.finish() }
        projectsViewModelSimple= ViewModelProvider(
            this,
            ProjectsViewModelSimpleFactory(AppDatabase.getDatabase(this).projectDao())
        )
            .get(ProjectsViewModelSimple::class.java)

        //liveProjectList.value=projectList
        val recycleView=findViewById<RecyclerView>(R.id.listrecyclerView)
        val layoutManager= LinearLayoutManager(this)
        recycleView.layoutManager=layoutManager
        adapter= ProjectAdapter(this, supportFragmentManager,this)
        projectsViewModelSimple.projectsListLiveData.observe(this,{
            projectList.clear()
            if(it!=null){
                projectList.addAll(it as Iterable<ProjectEntity>)
            }
            projectList.removeAt(0)
            adapter.submitList(projectList)
            adapter.notifyDataSetChanged()
        })

        recycleView.adapter=adapter
        linearLayout_List.setOnClickListener {
            var dialog = ListDialogCreate()
            var args = Bundle()
            args.putString("title", "添加清单")
            dialog.arguments = args
            dialog.show(supportFragmentManager, "listdialog")
        }

        var shp=getSharedPreferences("list_settings", Context.MODE_PRIVATE)
        switch_all.isChecked=shp.getBoolean("all",true)
        switch_today.isChecked=shp.getBoolean("today",true)
        switch_planed.isChecked=shp.getBoolean("planned",true)
        switch_important.isChecked=shp.getBoolean("important",true)
        switch_completed.isChecked=shp.getBoolean("completed",true)
        switch_collectbox.isChecked=shp.getBoolean("collectbox",true)
        switch_autohind.isChecked=shp.getBoolean("autohind",false)

        var editor=shp.edit()

        switch_all.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("all", isChecked)
            editor.apply()
        }
        switch_today.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("today", isChecked)
            editor.apply()
        }
        switch_planed.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("planned", isChecked)
            editor.apply()
        }
        switch_important.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("important", isChecked)
            editor.apply()
        }
        switch_completed.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("completed", isChecked)
            editor.apply()
        }
        switch_collectbox.setOnCheckedChangeListener{buttonView, isChecked->
            editor.putBoolean("collectbox",isChecked)
            editor.apply()
        }
        switch_autohind.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("autohind", isChecked)
            editor.apply()
        }


//        liveProjectList.observe(this,{
//            adapter.submitList(it)
//            adapter.notifyDataSetChanged()
//        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.notifyItemMoved(viewHolder.adapterPosition,target.adapterPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }).attachToRecyclerView(recycleView)
    }

}