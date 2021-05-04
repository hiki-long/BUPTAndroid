package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository

//删除所有todo数据库表项，危险，慎用
class DeleteAllToDosCase (
    val taskRepository: TaskRepository
){
    suspend operator fun invoke() = taskRepository.deleteAllTasks()
}