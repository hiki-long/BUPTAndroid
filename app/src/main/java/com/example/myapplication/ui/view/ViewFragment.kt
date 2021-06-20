package com.example.myapplication.ui.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.Project
import com.example.myapplication.ui.adapter.TaskAdapter
import com.example.myapplication.viewmodel.MainViewModel
import com.necer.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.fragment_view.*
import kotlinx.android.synthetic.main.select_endtime_remind.view.*
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class ViewFragment : Fragment() {

    private lateinit var viewViewModel: ViewViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter:TaskAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewViewModel =
                ViewModelProvider(this).get(ViewViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_view, container, false)
//        val root = inflater.inflate(R.layout.todo_calendar, container, false)
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskAdapter(activity as AppCompatActivity)
        (activity as MainActivity).needDrawer(false)
        postponeEnterTransition()
        calendar_recycleview.layoutManager = LinearLayoutManager(requireActivity())
        calendar_recycleview.adapter = adapter
        calendar_recycleview.doOnPreDraw {
            startPostponedEnterTransition()
        }

        myCalendar.setOnCalendarChangedListener { baseCalendar, year, month, localDate, dateChangeBehavior ->
            if(localDate != null){
                val calendarDate = CalendarUtil.getCalendarDate(localDate)
                activity?.setTitle(localDate.toString("yyyy年MM月dd日"))
            }else{
                activity?.setTitle("寸光阴")
            }

        }

        button.setOnClickListener {
            myCalendar.toToday()
        }


        var lastLiveData = mainViewModel.getTasks(0)
        lastLiveData!!.observe(
            viewLifecycleOwner,
            {
                var tasklist = it as ArrayList<TaskEntity>
                adapter.submitList(tasklist)
            }
        )

        adapter.taskClickListener = object : TaskAdapter.TaskClickListener {
            override fun updateTask(task: TaskEntity) {
                mainViewModel.updateTask(task)
            }

            override fun getProject(id: Int): Flow<Project?> {
                return mainViewModel.getList(id)
            }
        }


    }
}