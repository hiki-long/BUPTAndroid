package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task
import kotlinx.coroutines.flow.Flow

//获取一个list内所有的todo
class GetToDosInList(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int): Flow<List<Task?>> = taskRepository.getTaskInProject(id)
}