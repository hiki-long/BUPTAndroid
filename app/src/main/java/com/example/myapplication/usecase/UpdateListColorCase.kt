package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository

//更新清单的颜色
class UpdateListColorCase(
        val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(id: Int, color: Int) = projectRepository.modifyProjectColor(id, color)
}