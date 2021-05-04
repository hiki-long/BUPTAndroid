package com.example.myapplication.db.repository

import android.util.Log
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.mapper.ProjectMapper.toDomain
import com.example.myapplication.db.mapper.ProjectMapper.toEntity
import com.example.myapplication.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class ProjectRepository(private val projectDao: ProjectDao){
    fun getProjects(): Flow<List<Project?>> = projectDao.getProjects().map { list -> list.map { it.toDomain() } }

    fun getProject(id: Int): Flow<Project?> = projectDao.getProject(id).map { it.toDomain() }

    suspend fun deleteAllProject() = projectDao.deleteAllProject()

    suspend fun deleteProject(id: Int) = projectDao.deleteProject(id)

    suspend fun insert(project: Project): Long? = project.toEntity()?.let {
        val projectList=projectDao.getProjectsByName(it.project_name)
        if(!projectList.isEmpty()){
            return -1.toLong()
        }
        else{
            projectDao.insertProject(it)
        }
    }

    suspend fun modifyProjectColor(id: Int, color: Int) = projectDao.setProjectColor(id, color)

    suspend fun modifyProjectName(id: Int, name: String) = projectDao.setProjectName(id, name)

    suspend fun updateProject(project: Project) = project.toEntity()?.let {
        projectDao.updateProject(
            it
        )
    }
}