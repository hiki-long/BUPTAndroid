package com.example.myapplication.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.dao.TaskDao
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit;


class TasksViewModelSimple(taskDao: TaskDao): ViewModel() {
    var tasksLiveData=taskDao.getAllTasksLiveData()
    @RequiresApi(Build.VERSION_CODES.O)
    val timeLowerBar=OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
    @RequiresApi(Build.VERSION_CODES.O)
    val timeUpperBar=timeLowerBar.plusDays(1)
    var todayTasksLiveData=taskDao.getSomeDayTasksLiveData(timeLowerBar,timeUpperBar)
    val plannedTasksLiveData=taskDao.getPlannedTasksLiveData()
    val importantTasksLiveData=taskDao.getImportantTasksLiveData()
    val finishedTasksLiveData=taskDao.getFinshedTasksLiveData()

}