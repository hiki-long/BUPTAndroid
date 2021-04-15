package com.example.myapplication.db.view

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.example.myapplication.db.entity.TaskEntity


@DatabaseView(
    "SELECT t.*, p.project_name as project_name, p.project_color as project_color " +
            "FROM Task t LEFT JOIN Project p ON t.project_id = p.project_id "  +
            "ORDER BY project_id"
)
data class TaskProjectView(
    @Embedded val task: TaskEntity,
    @ColumnInfo(name = "project_name") val projectName: String,
    @ColumnInfo(name = "project_color") val projectColor: Int
)