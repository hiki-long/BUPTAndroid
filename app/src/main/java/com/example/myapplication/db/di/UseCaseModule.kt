package com.example.myapplication.db.di

import com.example.myapplication.db.repository.ProjectRepository
import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.usecase.*
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

    @Provides
    @ViewModelScoped
    fun provideGetProject(
            projectRepository: ProjectRepository
    ) =  GetListCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdateProject(
            projectRepository: ProjectRepository
    ) = UpdateListCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdateProjectName(
            projectRepository: ProjectRepository
    ) = UpdateListNameCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdateProjectColor(
            projectRepository: ProjectRepository
    ) = UpdateListColorCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteOneProject(
            projectRepository: ProjectRepository
    ) = DeleteListCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteAllProject(
            projectRepository: ProjectRepository
    ) = DeleteAllListsCase(projectRepository)

    @Provides
    @ViewModelScoped
    fun provideDeleteTask(
            taskRepository: TaskRepository
    ) = DeleteAllToDosCase(taskRepository)

    @Provides
    @ViewModelScoped
    fun provideDeletToDo(
            taskRepository: TaskRepository
    ) = DeleteTodoCase(taskRepository)


    @Provides
    @ViewModelScoped
    fun provideGetTasks(
            taskRepository: TaskRepository
    ) = GetTasksCase(taskRepository)

    @Provides
    @ViewModelScoped
    fun provideUpdateTask(
        taskRepository: TaskRepository
    ) = UpdateTaskCase(taskRepository)
}