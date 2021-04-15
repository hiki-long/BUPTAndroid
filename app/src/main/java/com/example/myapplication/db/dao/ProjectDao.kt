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

    //Color不清楚表示形式，这里暂定用十六进制的RGB来表示
    @Query("Update project SET project_color = :color")
    fun setProjectColor(color: Int)

    @Query("Update project SET project_name = :name WHERE project_id = :id")
    fun setProjectName(id: Int, name: String)

    @Update
    fun updateProject(project: ProjectEntity)
}