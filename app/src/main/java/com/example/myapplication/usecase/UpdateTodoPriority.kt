package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.TaskPriority

//更新todo优先级
class UpdateTodoPriority(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int,priority: TaskPriority) = taskRepository.setTaskPriority(id, priority)
}