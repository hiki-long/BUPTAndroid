package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task

//更新todo所有状态
class UpdateTodo(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = taskRepository.updateTask(task)
}