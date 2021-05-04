package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository

//删除project清单表中指定清单id的数据
class DeleteListCase (
        val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(id: Int) = projectRepository.deleteProject(id)
}