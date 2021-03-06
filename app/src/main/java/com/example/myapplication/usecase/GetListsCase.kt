package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.model.Project
import kotlinx.coroutines.flow.Flow

//获取所有的list信息
class GetListsCase (
        val projectRepository: ProjectRepository
) {
    operator fun invoke(): Flow<List<Project?>> = projectRepository.getProjects()
}