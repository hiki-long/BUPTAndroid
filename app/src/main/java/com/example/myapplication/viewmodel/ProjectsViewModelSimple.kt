package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.model.Task

class ProjectsViewModelSimple(val projectDao: ProjectDao):ViewModel() {
    var projectsListLiveData=projectDao.getProjectsListLiveData()

    fun deleteAProject(project_id:Int):Unit{
        projectDao.deleteProject(project_id)
    }

    fun updateAProject(project_id:Int,project_name: String?, project_color: Int?):Boolean{
        val projectsList=projectDao.getProjectsList()
        var repeated=false
        projectsList.forEach {
            if(it.project_name==project_name){
                repeated=true
            }
        }
        if(!repeated){
            val projectEntity=ProjectEntity(project_id=project_id,
                project_name=project_name?:"",project_color?:0)
            projectDao.updateProject(projectEntity)
            return true
        }
        else{
            return false
        }

    }

    fun insertAProject(project_name: String,
                       project_color: Int?):Long{
        val projectsList=projectDao.getProjectsList()
        var repeated=false
        projectsList.forEach {
            if(it.project_name==project_name){
                repeated=true
            }
        }
        if(!repeated){
            val insertProject=ProjectEntity(project_name = project_name,project_color = project_color)
            return projectDao.insertProject(insertProject)
        }
        else{
            return -1L
        }

    }
}