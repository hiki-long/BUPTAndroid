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
        }
        else if(mode == 2) {
            //选择时间段
            view = inflater.inflate(R.layout.select_time_fragment, container, false) as View
            val timebeginpicker = view.findViewById(R.id.dateTimePicker_begin) as DateTimePicker
            val timeendpicker = view.findViewById(R.id.dateTimePicker_end) as DateTimePicker
        }
        else {
            view = inflater.inflate(R.layout.select_time_picker, container, false) as View
            val timepicker = view.findViewById(R.id.dateTimePicker) as DateTimePicker
            timepicker.setLayout(R.layout.layout_date_picker)
            timepicker.setLabelText("年","月","日","时","分")
            timepicker.setDefaultMillisecond(System.currentTimeMillis())
            timepicker.showLabel(true)
            timepicker.setOnDateTimeChangedListener { milliseoncd ->  }
        }

        return view
    }
}