package com.example.myapplication.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Project")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) var project_id: Int = 0,
    var project_name: String,
    var project_color: Int?
) {

    override fun toString(): String {
        return project_name
    }
}
