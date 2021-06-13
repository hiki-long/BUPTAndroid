package com.example.myapplication.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.entity.TaskEntity
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit;


class TasksViewModelSimple(val taskDao: TaskDao): ViewModel() {
    var tasksLiveData=taskDao.getAllTasksLiveData()
    val plannedTasksLiveData=taskDao.getPlannedTasksLiveData()
    val importantTasksLiveData=taskDao.getImportantTasksLiveData()
    val finishedTasksLiveData=taskDao.getFinshedTasksLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun todayTasksLiveData(): LiveData<List<TaskEntity>> {
        val timeLowerBar=OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val timeUpperBar=timeLowerBar.plusDays(1)
        var todayTasksLiveData=taskDao.getSomeDayTasksLiveData(timeLowerBar,timeUpperBar)
        return todayTasksLiveData
    }

    fun getTaskLiveDataOfTaskId(taskId:Int):LiveData<TaskEntity> = taskDao.getTaskLiveDataOfTaskId(taskId)

}