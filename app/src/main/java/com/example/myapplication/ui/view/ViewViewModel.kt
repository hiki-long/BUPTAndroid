package com.example.myapplication.ui.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is view Fragment"
    }
    val text: LiveData<String> = _text
}