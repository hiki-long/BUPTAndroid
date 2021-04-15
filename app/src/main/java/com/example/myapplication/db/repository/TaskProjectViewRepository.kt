package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.TaskProjectViewDao
import com.example.myapplication.db.view.TaskProjectView
import kotlinx.coroutines.flow.Flow

class TaskProjectViewRepository(private  val taskProjectViewDao: TaskProjectViewDao) {
    fun getTaskProjectList(): Flow<List<TaskProjectView>> = taskProjectViewDao.getTaskProjectList()
}