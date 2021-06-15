package com.example.myapplication.ui.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.necer.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view.*
import kotlinx.android.synthetic.main.select_endtime_remind.view.*

@AndroidEntryPoint
class ViewFragment : Fragment() {

    private lateinit var viewViewModel: ViewViewModel

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
        (activity as MainActivity).needDrawer(false)

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





    }
}