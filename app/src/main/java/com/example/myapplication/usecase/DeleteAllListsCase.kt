package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository

//删除project清单表中所有的数据，慎用
class DeleteAllListsCase(
        val projectRepository: ProjectRepository
) {
    suspend operator fun invoke() = projectRepository.deleteAllProject()
}