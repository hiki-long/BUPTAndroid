package com.example.myapplication.db.di

import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.usecase.GetListsCase
import com.example.myapplication.usecase.InsertListCase
import com.example.myapplication.usecase.InsertTodoCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideInsertTodo(
            taskRepository: TaskRepository
    ) = InsertTodoCase(taskRepository)

    @Provides
    @ViewModelScoped
    fun provideInsertProject(
            projectRepository: ProjectRepository
    ) = InsertListCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideGetProjects(
            projectRepository: ProjectRepository
    ) = GetListsCase(projectRepository)

}