package com.example.myapplication.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskProjectRelation(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "project_id",
        entity = TaskEntity::class,
        entityColumn = "project_id"
    )
    val tasks: List<TaskEntity>
)