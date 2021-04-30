package com.example.myapplication.db.di

import android.app.Application
import androidx.room.Room
import com.example.myapplication.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseMoudle {

    @Singleton
    @Provides
    fun provideDatabase(application: Application) =
        Room.databaseBuilder(application, AppDatabase::class.java, "Database.db")
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideProjectDao(db: AppDatabase) = db.projectDao()

    @Provides
    fun provideTaskDao(db: AppDatabase) = db.taskDao()

    @Provides
    fun provideTaskProjectView(db: AppDatabase) = db.taskProjectViewDao()
}