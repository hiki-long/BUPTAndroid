package com.example.myapplication.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import kotlinx.android.synthetic.main.todo_calendar.*

class TimeSelectDialogCreate : DialogFragment() {
    private var mode : Int? = null
    private var title: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    //这里是弹出提前提醒的选择框

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.select_endtime_remind, container, false) as View
        val selecttime = view.findViewById(R.id.select_fit_time) as Button
        val cancel = view.findViewById(R.id.common_cancel) as Button
        val confirm = view.findViewById(R.id.set_remind_button) as Button
        cancel.setOnClickListener {
            dismiss()
        }
        confirm.setOnClickListener {
            dismiss()
        }
        selecttime.setOnClickListener {
            val temp = Bundle()
            temp.putInt("mode", 3)
            val timepickdialog = TimeBarDialogCreate()
            timepickdialog.arguments = temp
            timepickdialog.show(parentFragmentManager, "timeselect")
            dismiss()
        }

        return view
    }
}