package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository

//将todo移动到另一个list
class MoveTaskToAnotherList(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int, project_id: Int) = taskRepository.setTaskToAnotherProject(id, project_id)
}