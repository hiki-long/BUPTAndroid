package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import java.time.OffsetDateTime

//更新截止时间结束时间
class UpdateTodoEnd(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id:Int, time: OffsetDateTime?) = taskRepository.setTaskExecuteEnd(id, time)
}