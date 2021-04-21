package com.example.myapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.Project
import com.example.myapplication.ui.adapter.ProjectAdapter
import kotlinx.android.synthetic.main.activity_main.*

class SettingListsFragment : Fragment() {
    private var projectList=ArrayList<Project>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for(i in 0..4){
            projectList.add(Project(i,"${i}号清单",0))
        }
        val recycleView=requireActivity().findViewById<RecyclerView>(R.id.listrecyclerView)
        val layoutManager= LinearLayoutManager(requireActivity())
        recycleView.layoutManager=layoutManager
        val adapter= ProjectAdapter(requireContext(),projectList)
        recycleView.adapter=adapter

        (activity as MainActivity).needDrawer(false)
    }

}