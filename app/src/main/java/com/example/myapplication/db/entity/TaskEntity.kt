package com.example.myapplication.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import java.time.OffsetDateTime

@Entity(tableName = "Task")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true) var todo_id: Int = 0,
    var todo_create_time: OffsetDateTime,
    var todo_state: TaskState,
    var todo_name: String,
    var project_id: Int,
    var todo_priority: TaskPriority,
    var todo_execute_starttime: OffsetDateTime?,
    var todo_execute_endtime: OffsetDateTime?,
    var todo_execute_remind: OffsetDateTime?,
    var todo_deadline: OffsetDateTime?,
    var todo_deadline_remind: OffsetDateTime?,
    var todo_description: String?
    )