package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.db.dao.ProjectDao

class ProjectsViewModelSimple(projectDao: ProjectDao):ViewModel() {
    var projectsListLiveData=projectDao.getProjectsListLiveData()
}