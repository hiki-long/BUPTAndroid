package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.model.Project

//更新清单的颜色和名字
class UpdateListCase (
        val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(project: Project) = projectRepository.updateProject(project)
}