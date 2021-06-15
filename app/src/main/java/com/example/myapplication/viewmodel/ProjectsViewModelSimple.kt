package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.model.Task

class ProjectsViewModelSimple(val projectDao: ProjectDao):ViewModel() {
    var projectsListLiveData=projectDao.getProjectsListLiveData()

    fun updateAProject(project_id:Int,project_name: String?, project_color: Int?){
        val projectEntity=ProjectEntity(project_id=project_id,
        project_name=project_name?:"",project_color?:0)
        projectDao.updateProject(projectEntity)
    }

    fun insertAProject(project_name: String,
                       project_color: Int?):Long{
        val insertProject=ProjectEntity(project_name = project_name,project_color = project_color)
        return projectDao.insertProject(insertProject)
    }
}