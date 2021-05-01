package com.example.myapplication.db.di

import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.dao.TaskProjectViewDao
import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.db.repository.TaskProjectViewRepository
import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao) = TaskRepository(taskDao)

    @Provides
    @Singleton
    fun provideProjectRepository(projectDao: ProjectDao) = ProjectRepository(projectDao)

    @Provides
    @Singleton
    fun provideTaskProjectViewRepository(taskProjectViewDao: TaskProjectViewDao) = TaskProjectViewRepository(taskProjectViewDao)

}