package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository

//更新todo描述
class UpdateTodoDesc(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int, description: String?) = taskRepository.setTaskDesc(id, description)
}