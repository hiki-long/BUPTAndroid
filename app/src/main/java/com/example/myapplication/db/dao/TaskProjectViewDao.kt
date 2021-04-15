package com.example.myapplication.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.db.view.TaskProjectView
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskProjectViewDao {

    @Query("SELECT * FROM TaskProjectView")
    fun getTaskProjectList(): Flow<List<TaskProjectView>>
}