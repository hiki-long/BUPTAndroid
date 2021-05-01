package com.example.myapplication.model

data class Project(
    val project_id: Int = 0,
    val project_name: String,
    val project_color: Int?,
    val tasks: List<Task?> = emptyList()
)