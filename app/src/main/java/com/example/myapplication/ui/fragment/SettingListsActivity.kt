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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.Project
import com.example.myapplication.ui.adapter.ProjectAdapter
import com.example.myapplication.ui.todo.ListDialogCreate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_setting_lists.*

@AndroidEntryPoint
class SettingListsActivity : AppCompatActivity() {
    private var projectList=ArrayList<Project>()
//    private lateinit var liveProjectList:LiveData<ArrayList<Project>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_lists)
        //设置toolbar
        setSupportActionBar(setting_toolbar)
        setting_toolbar.title = "清单设置"
        setting_toolbar.setNavigationIcon(R.drawable.ic_back_24)
        setting_toolbar.setNavigationOnClickListener { this.finish() }

        for(i in 0..6){
            projectList.add(Project(i,"${i}号清单",0))
        }
        //liveProjectList.value=projectList
        val recycleView=findViewById<RecyclerView>(R.id.listrecyclerView)
        val layoutManager= LinearLayoutManager(this)
        recycleView.layoutManager=layoutManager
        val adapter= ProjectAdapter(this, supportFragmentManager)
        adapter.submitList(projectList)
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
        switch_planed.isChecked=shp.getBoolean("planed",true)
        switch_important.isChecked=shp.getBoolean("important",true)
        switch_completed.isChecked=shp.getBoolean("completed",true)
        switch_autohind.isChecked=shp.getBoolean("autohind",true)
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
            editor.putBoolean("planed", isChecked)
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
//                var wordFrom=allWords.get(viewHolder.adapterPosition)
//                var wordTo=allWords.get(target.adapterPosition)
//                var idtemp=wordFrom.id
//                wordFrom.id=wordTo.id
//                wordTo.id=idtemp
//                wordViewModel.updateWords(wordFrom,wordTo)
                adapter.notifyItemMoved(viewHolder.adapterPosition,target.adapterPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                var word=allWords.get(viewHolder.adapterPosition)
//                wordViewModel.deleteWords(word)
//                var p = projectList.removeAt(viewHolder.adapterPosition)
//                Snackbar.make(findViewById(R.id.activity_setting_lists),"删除一条清单", Snackbar.LENGTH_SHORT)
//                        .setAction("撤销"){
//                            projectList.add(p)
//                            adapter.notifyDataSetChanged()
//                        }.show()
//                adapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(recycleView)
    }

}