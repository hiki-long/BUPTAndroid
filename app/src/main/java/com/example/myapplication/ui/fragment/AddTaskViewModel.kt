package com.example.myapplication.ui.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(

): ViewModel() {
    var pre_time: MutableLiveData<String> = MutableLiveData() //提醒提前多久
    var pre_time2: MutableLiveData<String> = MutableLiveData() //执行提醒提前多久
    var time_point: MutableLiveData<OffsetDateTime?> = MutableLiveData() //时间点
    var time_point2: MutableLiveData<OffsetDateTime?> = MutableLiveData() //时间端的第二个时间点
    var time_point3: MutableLiveData<OffsetDateTime?> = MutableLiveData() //自定义时间点
}