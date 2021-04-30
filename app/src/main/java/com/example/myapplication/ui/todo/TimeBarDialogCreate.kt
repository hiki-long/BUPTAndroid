package com.example.myapplication.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimePicker
import kotlinx.android.synthetic.main.select_time_picker.*

class TimeBarDialogCreate : DialogFragment() {
    //这里是时间选择框弹出的对话框,根据时间选择有3种模式
    /*
    * 1.当天时间
    * 2.时间段选择
    * 3.自定义时间
    *
    * */
    private var mode :Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(arguments != null) {
            mode = arguments?.getInt("mode")
            Log.d("message",mode.toString())
        }
        var view : View
        if(mode == 1) {
            //选择时间点
            view = inflater.inflate(R.layout.select_time_picker, container, false) as View
            val timepicker = view.findViewById(R.id.dateTimePicker) as DateTimePicker
            timepicker.setLabelText("年","月","日","时","分")
            timepicker.setDisplayType(intArrayOf(
                DateTimeConfig.HOUR,
                DateTimeConfig.MIN
            ))
            timepicker.setDefaultMillisecond(System.currentTimeMillis())
            timepicker.showLabel(true)
            timepicker.setOnDateTimeChangedListener { milliseoncd ->  }
            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select) as Button
            confirm.setOnClickListener {
                dismiss()
            }
        }
        else if(mode == 2) {
            //选择时间段
            view = inflater.inflate(R.layout.select_time_fragment, container, false) as View
//            val timebeginpicker = view.findViewById(R.id.dateTimePicker_begin) as DateTimePicker
//            val timeendpicker = view.findViewById(R.id.dateTimePicker_end) as DateTimePicker
            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select2) as Button
            confirm.setOnClickListener {
                dismiss()
            }

        }
        else {
            //显示自定义时间
            view = inflater.inflate(R.layout.select_time_picker, container, false) as View
            val timepicker = view.findViewById(R.id.dateTimePicker) as DateTimePicker
            timepicker.setLayout(R.layout.layout_date_picker)
            timepicker.setLabelText("年","月","日","时","分")
            timepicker.setDefaultMillisecond(System.currentTimeMillis())
            timepicker.showLabel(true)
            timepicker.setOnDateTimeChangedListener { milliseoncd ->  }
            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select) as Button
            confirm.setOnClickListener {
                dismiss()
            }
        }

        return view
    }
}