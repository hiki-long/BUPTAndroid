package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class APP: Application(){

    private  val appCoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    companion object{
        const val PACKAGE = "com.example.myapplication"
    }

    override fun onCreate(){
        super.onCreate()
    }
}