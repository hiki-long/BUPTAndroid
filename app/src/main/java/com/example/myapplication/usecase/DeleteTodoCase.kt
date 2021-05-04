package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository

//删除单个todo
class DeleteTodoCase(
     val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int) = taskRepository.deleteTask(id)
}