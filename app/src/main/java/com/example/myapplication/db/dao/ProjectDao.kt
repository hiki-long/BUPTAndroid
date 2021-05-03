package com.example.myapplication.db.dao

import androidx.room.*
import com.example.myapplication.db.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project WHERE project_id = :id")
    fun getProject(id: Int): Flow<ProjectEntity>

    @Query("SELECT * FROM project ORDER BY project_id")
    fun getProjects(): Flow<List<ProjectEntity>>

    @Query("DELETE FROM project WHERE project_id = :id")
    fun deleteProject(id: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProject(project: ProjectEntity): Long

    //Color表示为0xFF(R,G,B)的数值，因此Int是负数
    @Query("Update project SET project_color = :color WHERE project_id = :id")
    fun setProjectColor(id: Int, color: Int)

    @Query("Update project SET project_name = :name WHERE project_id = :id")
    fun setProjectName(id: Int, name: String)

    @Update
    fun updateProject(project: ProjectEntity)
}