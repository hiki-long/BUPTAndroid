package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.db.mapper.TaskMapper.toDomain
import com.example.myapplication.db.mapper.TaskMapper.toEntity
import com.example.myapplication.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(private val taskDao: TaskDao) {
    fun getTask(id:Int): Flow<Task?> = taskDao.getTask(id).map {it.toDomain()}

    fun getTasks(): Flow<List<TaskEntity>> =taskDao.getTasks()

    fun getTasks(): Flow<List<TaskEntity>> =taskDao.getTasks()

    suspend fun deleteTask(id: Int) = taskDao.deleteTask(id)

    suspend fun insert(task: Task):Long ? = task.toEntity()?.let { taskDao.insertTask(it) }

    suspend fun setTaskDone(id: Int) = taskDao.setTaskDone(id)

    suspend fun setTaskDoing(id: Int) = taskDao.setTaskDoing(id)

    suspend fun updateTask(task: Task) = task.toEntity()?.let { taskDao.updateTask(it) }

}