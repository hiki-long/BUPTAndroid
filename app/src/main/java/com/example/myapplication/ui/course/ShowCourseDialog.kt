package com.example.myapplication.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.AddTaskViewModel
import com.example.myapplication.ui.todo.TimeBarDialogCreate
import com.example.myapplication.ui.todo.TimeSelectDialogCreate
import com.example.myapplication.ui.uti.UtiFunc

class ShowCourseDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view =  inflater.inflate(R.layout.show_course, container, false) as View
        if (arguments != null) {
            val coursename = view.findViewById<TextView>(R.id.edit_course)
            coursename.text = requireArguments().getString("name")
            val courseday = view.findViewById<TextView>(R.id.edit_day)
            courseday.text = requireArguments().getString("day")
            val courssbegin = view.findViewById<TextView>(R.id.edit_course_begin)
            courssbegin.text = requireArguments().getString("start")
            val coursend = view.findViewById<TextView>(R.id.edit_course_end)
            coursend.text = requireArguments().getString("end")
            val courseteacher = view.findViewById<TextView>(R.id.edit_teacher)
            courseteacher.text = requireArguments().getString("teacher")
            val courseplace = view.findViewById<TextView>(R.id.edit_place)
            courseplace.text = requireArguments().getString("room")
            val courseweekbegin = view.findViewById<TextView>(R.id.edit_week_begin)
            courseweekbegin.text = requireArguments().getInt("weekstart").toString()
            val courseweekend = view.findViewById<TextView>(R.id.edit_week_end)
            courseweekend.text = requireArguments().getInt("weekend").toString()
        }
        val cancel = view.findViewById<Button>(R.id.edit_cancel)
        cancel.setOnClickListener {
            dismiss()
        }
        return view
    }

}