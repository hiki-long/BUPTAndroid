package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository;
import com.example.myapplication.model.Project;
import com.example.myapplication.model.Task

class InsertListCase (private val projectRepository: ProjectRepository) {
    suspend operator fun invoke(
            project_name: String,
            project_color: Int?,
            tasks: List<Task?> = emptyList()
    ):Long? =  projectRepository.insert(Project(
            project_name = project_name,
            project_color = project_color,
            tasks = tasks
    ))
}
