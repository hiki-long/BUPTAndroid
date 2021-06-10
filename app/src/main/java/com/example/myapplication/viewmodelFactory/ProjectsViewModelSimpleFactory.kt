package com.example.myapplication.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.db.dao.ProjectDao
import java.lang.Exception

class ProjectsViewModelSimpleFactory(private val projectDao: ProjectDao):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel:ViewModel?=null
        var classname=modelClass.name
        try{
            viewModel=Class.forName(classname).getConstructor(ProjectDao::class.java).newInstance(projectDao) as ViewModel

        }catch (e:Exception){
            e.printStackTrace()
        }
        return viewModel as T
    }
}