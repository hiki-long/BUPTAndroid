package com.example.myapplication.usecase

import com.example.myapplication.db.repository.ProjectRepository

//更新清单的名字
class UpdateListNameCase (
    val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(id:Int, name: String) = projectRepository.modifyProjectName(id, name)
}