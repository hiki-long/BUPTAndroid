package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Project
import com.example.myapplication.ui.adapter.ProjectAdapter

class SettingListsActivity : AppCompatActivity() {
    private var projectList=ArrayList<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_lists)
        for(i in 0..4){
            projectList.add(Project(i,"${i}号清单",0))
        }
        val recycleView=findViewById<RecyclerView>(R.id.listrecyclerView)
        val layoutManager= LinearLayoutManager(this)
        recycleView.layoutManager=layoutManager
        val adapter= ProjectAdapter(this,projectList)
        recycleView.adapter=adapter
    }
}