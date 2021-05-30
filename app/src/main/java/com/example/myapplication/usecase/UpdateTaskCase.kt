package com.example.myapplication.usecase

import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.db.repository.TaskRepository

class UpdateTaskCase(
    val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: TaskEntity) = taskRepository.updateTaskEntity(task)
}