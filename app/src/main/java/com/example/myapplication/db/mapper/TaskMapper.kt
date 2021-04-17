package com.example.myapplication.db.mapper

import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.Task

object TaskMapper {
    fun TaskEntity?.toDomain(): Task? = this?.let {
        Task(
            todo_id = it.todo_id,
            todo_create_time = it.todo_create_time,
            todo_state = it.todo_state,
            todo_name = it.todo_name,
            project_id = it.project_id,
            todo_priority = it.todo_priority,
            todo_execute_starttime = it.todo_execute_starttime,
            todo_execute_endtime = it.todo_execute_endtime,
            todo_execute_remind = it.todo_execute_remind,
            todo_deadline = it.todo_deadline,
            todo_deadline_remind = it.todo_deadline_remind,
            todo_description = it.todo_description
        )
    }

    fun Task?.toEntity(): TaskEntity? = this?.let{
        TaskEntity(
            todo_id = it.todo_id,
            todo_create_time = it.todo_create_time,
            todo_state = it.todo_state,
            todo_name = it.todo_name,
            project_id = it.project_id,
            todo_priority = it.todo_priority,
            todo_execute_starttime = it.todo_execute_starttime,
            todo_execute_endtime = it.todo_execute_endtime,
            todo_execute_remind = it.todo_execute_remind,
            todo_deadline = it.todo_deadline,
            todo_deadline_remind = it.todo_deadline_remind,
            todo_description = it.todo_description
        )
    }
}