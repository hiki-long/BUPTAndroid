package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import java.time.OffsetDateTime

//更新执行时间提醒
class UpdateExecuteRemind(
        val taskRepository: TaskRepository
) {
    suspend operator fun invoke(id: Int, time: OffsetDateTime?) = taskRepository.setTaskExecuteRemind(id, time)
}