package com.example.myapplication.ui.todo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.ui.fragment.AddTaskActivity
import com.example.myapplication.ui.fragment.AddTaskViewModel
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.DateTimePicker
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class TimeBarDialogCreate : DialogFragment() {
    private var CurrentTime: Long? = null
    private lateinit var viewmodel: AddTaskViewModel
    private var RecordValue: OffsetDateTime? = null
    private var RecordValue2: OffsetDateTime? = null
    private var RecordValue3: OffsetDateTime? = null
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
        viewmodel = ViewModelProvider(requireActivity()).get(AddTaskViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun FormatTime(offset: Long?): OffsetDateTime?
    {
        if (offset != null) {
            return OffsetDateTime.ofInstant(Instant.ofEpochMilli(offset + CurrentTime!!), ZoneOffset.UTC)
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(arguments != null) {
            mode = arguments?.getInt("mode")
//            Log.d("message",mode.toString())
        }
        var view : View
        //加上8小时是东八区的时间
        val hour = 8
        //加上小时的偏移才是正常时间
        CurrentTime = hour.toLong() * 60 * 60 * 1000
        Log.d("message",hour.toLong().toString())
        if(mode == 1) {
            //选择时间点
            view = inflater.inflate(R.layout.select_time_picker, container, false) as View
            val timepicker = view.findViewById(R.id.dateTimePicker) as DateTimePicker
            timepicker.setLabelText("年","月","日","时","分")
            timepicker.setDisplayType(intArrayOf(
                DateTimeConfig.HOUR,
                DateTimeConfig.MIN
            ))
            timepicker.showLabel(true)
            timepicker.setOnDateTimeChangedListener { milliseoncd ->
                run {
                    RecordValue = FormatTime(milliseoncd)
                    RecordValue2 = FormatTime(milliseoncd + 3600000)
                }
            }
            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select) as Button
            confirm.setOnClickListener {
                viewmodel.time_point.value = RecordValue
                viewmodel.time_point2.value = RecordValue2
                dismiss()
            }


        }
        else if(mode == 2) {
            //选择时间段
            view = inflater.inflate(R.layout.select_time_fragment, container, false) as View
            val timebeginpicker = view.findViewById(R.id.dateTimePicker_begin) as DateTimePicker
            val timeendpicker = view.findViewById(R.id.dateTimePicker_end) as DateTimePicker
            timeendpicker.setDefaultMillisecond(System.currentTimeMillis() + 3600000)
            timebeginpicker.setOnDateTimeChangedListener { milliseoncd ->
                RecordValue = FormatTime(milliseoncd)
            }
            timeendpicker.setOnDateTimeChangedListener {milliseoncd ->
                RecordValue2 = FormatTime(milliseoncd)
            }

            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select2) as Button
            confirm.setOnClickListener {
                if (RecordValue!! > RecordValue2!!) {
                    Toast.makeText(context,"起始时间不能大于结束时间",Toast.LENGTH_SHORT).show()
                } else {
                    viewmodel.time_point.value = RecordValue
                    viewmodel.time_point2.value = RecordValue2
                    dismiss()
                }
            }

        }
        else {
            //显示自定义时间
            view = inflater.inflate(R.layout.select_time_picker, container, false) as View
            val timepicker = view.findViewById(R.id.dateTimePicker) as DateTimePicker
            timepicker.setLayout(R.layout.layout_date_picker)
            timepicker.setLabelText("年","月","日","时","分")
            timepicker.showLabel(true)
            timepicker.setOnDateTimeChangedListener { milliseoncd ->
                RecordValue3 = FormatTime(milliseoncd)
            }
            val cancel = view.findViewById(R.id.common_cancel) as Button
            cancel.setOnClickListener {
                dismiss()
            }
            val confirm = view.findViewById(R.id.confirm_time_select) as Button
            confirm.setOnClickListener {
                viewmodel.time_point3.value = RecordValue3
                dismiss()
            }
        }

        return view
    }
}