package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.model.Project
import kotlinx.coroutines.flow.Flow

//获得指定id的清单信息
class GetListCase (
        val projectRepository: ProjectRepository
) {
    operator fun invoke(id: Int): Flow<Project?> = projectRepository.getProject(id)
}