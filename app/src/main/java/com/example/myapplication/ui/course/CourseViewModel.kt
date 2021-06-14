package com.example.myapplication.ui.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(

): ViewModel() {
    var colorrandom: MutableLiveData<Int> = MutableLiveData()
    var day: MutableLiveData<Int> = MutableLiveData()
    var id: MutableLiveData<Int> = MutableLiveData()
    var name: MutableLiveData<String> = MutableLiveData()
    var room: MutableLiveData<String> = MutableLiveData()
    var start: MutableLiveData<Int> = MutableLiveData()
    var step: MutableLiveData<Int> = MutableLiveData()
    var teacher: MutableLiveData<String> = MutableLiveData()
    var term: MutableLiveData<String> = MutableLiveData()
    var weekList: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    var change: MutableLiveData<Boolean> = MutableLiveData()
    var deletelist: MutableLiveData<MutableSet<Int>> = MutableLiveData()
    var deletedone: MutableLiveData<Boolean> = MutableLiveData()
}