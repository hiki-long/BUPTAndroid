package com.example.myapplication.model

import java.time.OffsetDateTime

data class TaskProject (
    val task_id: Int = 0,
    val todo_create_time: OffsetDateTime,
    val todo_state: TaskState,
    val todo_name: String,
    val project_id : Int,
    val todo_priority: TaskPriority,
    val todo_execute_starttime: OffsetDateTime,
    val todo_execute_endtime: OffsetDateTime,
    val todo_execute_remind: OffsetDateTime?,
    val todo_deadline: OffsetDateTime,
    val todo_deadline_remind: OffsetDateTime,
    val todo_description: String?,
    val project_name: String,
    val project_color: Int
)