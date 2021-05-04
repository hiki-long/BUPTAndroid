package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.TaskState

//更新todo完成状态
class UpdateTodoState(
        val taskRepository: TaskRepository
){
    suspend operator fun invoke(id:Int, state: TaskState) = taskRepository.setTaskState(id, state)
}