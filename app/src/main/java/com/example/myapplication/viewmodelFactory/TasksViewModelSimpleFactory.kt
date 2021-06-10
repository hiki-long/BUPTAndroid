package com.example.myapplication.viewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.dao.TaskDao
import java.lang.Exception

class TasksViewModelSimpleFactory(private val taskDao: TaskDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var viewModel:ViewModel?=null
        var classname=modelClass.name
        try{
            //使用反射产生不同的viewmodel实例
            viewModel=Class.forName(classname).getConstructor(
                TaskDao::class.java
            ).newInstance(taskDao) as ViewModel
        }catch (e:Exception){
            e.printStackTrace()
        }
        return viewModel as T
    }
}