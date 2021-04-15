package com.example.myapplication.db.dao

import androidx.room.*
import com.example.myapplication.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("DELETE FROM task WHERE task_id = :id")
    fun deleteTask(id : Int)

    @Query("SELECT * FROM task WHERE task_id = :id")
    fun getTask(id: Int): Flow<TaskEntity>

    @Query("SELECT * FROM task ORDER BY todo_create_time")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: TaskEntity): Long

    //更新task状态
    @Query("UPDATE task SET todo_state = 'DOING' WHERE task_id = :id")
    fun setTaskDoing(id: Int)

    @Query("UPDATE task SET todo_state = 'DONE' WHERE task_id = :id")
    fun setTaskDone(id: Int)

    //更新task名字
    @Query("UPDATE task SET todo_name = :name WHERE task_id = :id")
    fun setTaskName(id: Int, name: String)

    @Update
    fun updateTask(task: TaskEntity)

}