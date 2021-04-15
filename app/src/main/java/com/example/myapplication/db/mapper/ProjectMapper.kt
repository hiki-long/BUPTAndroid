package com.example.myapplication.db.mapper

import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.db.entity.TaskProjectRelation
import com.example.myapplication.db.mapper.TaskMapper.toDomain
import com.example.myapplication.model.Project

object ProjectMapper {
    fun ProjectEntity?.toDomain(): Project? = this?.let {
        Project(
            project_id = it.project_id,
            project_name = it.project_name,
            project_color = it.project_color
        )
    }

    fun TaskProjectRelation?.toDomain(): Project? = this?.let{
        Project(
            project_id = it.project.project_id,
            project_name = it.project.project_name,
            project_color = it.project.project_color,
            tasks = it.tasks.map{ task -> task.toDomain() }.sortedBy { task -> task?.todo_create_time }
        )
    }

    fun Project?.toEntity(): ProjectEntity? = this?.let {
        ProjectEntity(
            project_id = it.project_id,
            project_name = it.project_name,
            project_color = it.project_color
        )
    }
}