package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import java.time.OffsetDateTime

//更新截止时间提醒
class UpdateDeadLineRemind(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int, time: OffsetDateTime?) = taskRepository.setTaskDeadLineRemind(id, time)
}