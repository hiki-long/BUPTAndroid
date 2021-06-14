package com.example.myapplication.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

class AddCourseDialog : DialogFragment() {
    private lateinit var viewmodel: CourseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view =  inflater.inflate(R.layout.add_course, container, false) as View
        val cancel = view.findViewById<Button>(R.id.edit_cancel)
        viewmodel = ViewModelProvider(requireActivity()).get(CourseViewModel::class.java)
        viewmodel.colorrandom.value = 2
        viewmodel.change.value = false
        cancel.setOnClickListener {
            viewmodel.change.value = false
            dismiss()
        }
        val confirm = view.findViewById<Button>(R.id.edit_confirm)
        confirm.setOnClickListener {
//            Toast.makeText(view.context, "confirm click", Toast.LENGTH_SHORT).show()
            val coursename = view.findViewById<EditText>(R.id.edit_course)
            viewmodel.name.value = coursename.text?.toString()

            val courseday = view.findViewById<EditText>(R.id.edit_day)
            viewmodel.day.value = courseday.text?.toString()?.toInt()

            val courssbegin = view.findViewById<EditText>(R.id.edit_course_begin)
            viewmodel.start.value = courssbegin.text?.toString()?.toInt()

            val coursend = view.findViewById<EditText>(R.id.edit_course_end)
            viewmodel.step.value =
                courssbegin.text?.toString()?.toInt()?.let { it1 ->
                    coursend.text?.toString()?.toInt()?.minus(
                        it1
                    )
                }

            val courseteacher = view.findViewById<EditText>(R.id.edit_teacher)
            viewmodel.teacher.value = courseteacher.text?.toString()

            val courseplace = view.findViewById<EditText>(R.id.edit_place)
            viewmodel.room.value = courseplace.text?.toString()

            val courseweekbegin = view.findViewById<EditText>(R.id.edit_week_begin)
            val beginw = courseweekbegin.text?.toString()?.toInt()
            val courseweekend = view.findViewById<EditText>(R.id.edit_week_end)
            val endw = courseweekend.text?.toString()?.toInt()
            val templist = ArrayList<Int>()
            if(beginw != null && endw != null) {
                for (i in beginw!!..endw!!) {
                    templist.add(i)
                }
            }
            viewmodel.weekList.value = templist
            viewmodel.change.value = true
            dismiss()
        }
        return view
    }

}