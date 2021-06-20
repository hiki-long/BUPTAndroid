package com.example.myapplication.viewmodel

import android.os.Build
import android.support.v4.app.INotificationSideChannel
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit;


class TasksViewModelSimple(val taskDao: TaskDao): ViewModel() {
    var tasksLiveData=taskDao.getAllTasksLiveData()
    val plannedTasksLiveData=taskDao.getPlannedTasksLiveData()
    val importantTasksLiveData=taskDao.getImportantTasksLiveData()
    val finishedTasksLiveData=taskDao.getFinshedTasksLiveData()
    val collecboxTaksLiveData=taskDao.getTasksLiveDataOfAProject(1)

    fun setTaskState(taskId: Int,taskState: TaskState)=taskDao.setTodoState(taskId,taskState)
    fun getTasksLiveDataOfAProject(project_id: Int)=taskDao.getTasksLiveDataOfAProject(project_id)

    fun deleteTasksOfAProject(project_id:Int):Unit{
        taskDao.deleteTasksOfAProject(project_id)
    }

    fun deleteTaskOfTaskId(taskId: Int){
        taskDao.deleteTask(taskId)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun todayTasksLiveData(): LiveData<List<TaskEntity>> {
        val timeLowerBar=OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val timeUpperBar=timeLowerBar.plusDays(1)
        var todayTasksLiveData=taskDao.getSomeDayTasksLiveData(timeLowerBar,timeUpperBar)
        return todayTasksLiveData
    }

    fun insertTask(todo_create_time: OffsetDateTime,
                       todo_state: TaskState,
                       todo_name: String,
                       project_id: Int,
                       todo_priority: TaskPriority,
                       todo_execute_starttime: OffsetDateTime?,
                       todo_execute_endtime: OffsetDateTime?,
                       todo_execute_remind: OffsetDateTime?,
                       todo_deadline: OffsetDateTime?,
                       todo_deadline_remind: OffsetDateTime?,
                       todo_description: String?):Long{
        val taskEntity=TaskEntity(todo_create_time = todo_create_time,todo_state = todo_state,todo_name = todo_name,
        project_id = project_id,todo_priority = todo_priority,todo_execute_starttime = todo_execute_starttime,
        todo_execute_endtime = todo_execute_endtime,todo_execute_remind = todo_execute_remind,todo_deadline = todo_deadline,
        todo_deadline_remind = todo_deadline_remind,todo_description = todo_description)
        return taskDao.insertTask(taskEntity)
    }

    fun getTaskLiveDataOfTaskId(taskId:Int):LiveData<TaskEntity> = taskDao.getTaskLiveDataOfTaskId(taskId)

}