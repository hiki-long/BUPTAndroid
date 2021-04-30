package com.example.myapplication.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import kotlinx.android.synthetic.main.todo_calendar.*
import java.util.zip.Inflater

class DaySelectDialogCreate : DialogFragment() {
    private var mode : Int? = null
    private var title: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null)
        {
            title = arguments?.getString("title")
            mode = arguments?.getInt("mode")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{
        val view =  inflater.inflate(R.layout.todo_calendar, container, false) as View
        val dialogtitle = view.findViewById(R.id.time_dialog_title) as TextView
        val todotime = view.findViewById(R.id.select_todo_time) as Button
        val remindtime = view.findViewById(R.id.select_remind_time) as Button
        val cancel = view.findViewById(R.id.common_cancel) as Button
        val confirm = view.findViewById(R.id.confirm_alltime_button) as Button
        dialogtitle?.text =  title
        if(mode == 1)
        {
            todotime.text = "设置时间点"
            remindtime.text = "截止时间提醒"
            todotime.setOnClickListener {
                val temp = Bundle()
                temp.putInt("mode", 1)
                val timedialog = TimeBarDialogCreate()
                timedialog.arguments = temp
                timedialog.show(parentFragmentManager, "ShowTimeBar")
            }
        }
        else
        {
            todotime.text = "设置时间段"
            remindtime.text = "执行时间提醒"
            todotime.setOnClickListener {
                val temp = Bundle()
                temp.putInt("mode", 2)
                val timedialog = TimeBarDialogCreate()
                timedialog.arguments = temp
                timedialog.show(parentFragmentManager, "ShowTimeBar")
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }

        confirm.setOnClickListener {
            dismiss()
        }

        remindtime.setOnClickListener {
            val optiondialog = TimeSelectDialogCreate()
            optiondialog.show(parentFragmentManager, "SelectOption")
        }
        return view
    }
}