package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskPriority
import kotlinx.coroutines.flow.Flow

//获取单个todo
class GetTodoCase(
    val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int): Flow<Task?> = taskRepository.getTask(id)
}