package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import java.time.OffsetDateTime

//更新截止时间开始时间
class UpdateTodoBegin(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int, time: OffsetDateTime?) = taskRepository.setTaskExecuteBegin(id, time)
}