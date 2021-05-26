package com.example.myapplication.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.AddTaskViewModel
import kotlinx.android.synthetic.main.todo_calendar.*

class TimeSelectDialogCreate : DialogFragment() {
    private var mode : Int? = null
    private var title: String? = null
    private lateinit var viewmodel: AddTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null)
        {
            mode = arguments?.getInt("mode")
        }
        viewmodel = ViewModelProvider(requireActivity()).get(AddTaskViewModel::class.java)
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
        selecttime.setOnClickListener {
            val temp = Bundle()
            temp.putInt("mode", 3)
            val timepickdialog = TimeBarDialogCreate()
            timepickdialog.arguments = temp
            timepickdialog.show(parentFragmentManager, "timeselect")
            dismiss()
        }
        val radio_group = view.findViewById<RadioGroup>(R.id.select_time_group)
//        radio_group.setOnCheckedChangeListener { group, checkedId ->
//            val radio: RadioButton = view.findViewById(checkedId)
//                    Toast.makeText(view.context," On checked change : ${radio.text}",
//                        Toast.LENGTH_SHORT).show()
//        }
        // Get radio group selected status and text using button click event
        confirm.setOnClickListener{
            // Get the checked radio button id from radio group
            var id: Int = radio_group.checkedRadioButtonId
            if (id!=-1){ // If any radio button checked from radio group
                // Get the instance of radio button using id
                val radio:RadioButton = view.findViewById(id)
                if(radio.text != "无"){
                    if(mode == 1)
                    {
                        viewmodel.pre_time.value = radio.text.toString()
                    }
                    else {
                        viewmodel.pre_time2.value = radio.text.toString()
                    }
                }
                else {
                    if(mode == 1)
                    {
                        viewmodel.pre_time.value = ""
                    }
                    else
                    {
                        viewmodel.pre_time2.value = ""
                    }
                }
            }else{
                // If no radio button checked in this radio group
            }
            dismiss()
        }
        return view
    }





}


