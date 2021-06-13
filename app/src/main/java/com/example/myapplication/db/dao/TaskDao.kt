package com.example.myapplication.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import kotlinx.coroutines.flow.Flow
import java.time.OffsetDateTime

@Dao
interface TaskDao {
    @Query("DELETE FROM task WHERE todo_id = :id")
    fun deleteTask(id : Int)

    @Query("SELECT * FROM task WHERE todo_id = :id")
    fun getTask(id: Int): Flow<TaskEntity>

    @Query("SELECT * FROM task")
    fun getAllTask():Flow<List<TaskEntity>>

    @Query("SELECT * FROM task")
    fun getAllTasksLiveData():LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_execute_starttime<=:timeUpperBar and todo_execute_endtime>=:timeLowerBound")
    fun getSomeDayTasksLiveData(timeLowerBound:OffsetDateTime,timeUpperBar:OffsetDateTime):LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_id=:taskId")
    fun getTaskLiveDataOfTaskId(taskId:Int):LiveData<TaskEntity>

    @Query("SELECT * FROM task WHERE todo_state='PLANNED'")
    fun getPlannedTasksLiveData():LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_priority='IMPORTANT'")
    fun getImportantTasksLiveData():LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state='FINISHED'")
    fun getFinshedTasksLiveData():LiveData<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state = 'DOING' ORDER BY datetime(todo_create_time)")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state = 'DOING' ORDER BY project_id")
    fun getTasksOrderByProject(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state = 'DOING' ORDER BY todo_deadline")
    fun getTasksOrderByDeadline(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state = 'DOING' ORDER BY todo_execute_starttime")
    fun getTasksOrderByExecuteTime(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE todo_state = 'DOING' ORDER BY todo_priority DESC")
    fun getTasksOrderByPriority(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTask(task: TaskEntity): Long

    //更新task状态
    @Query("UPDATE task SET todo_state = :todo_state WHERE todo_id = :todo_id")
    fun setTodoState(todo_id: Int, todo_state: TaskState)

    //更新task名字
    @Query("UPDATE task SET todo_name = :name WHERE todo_id = :id")
    fun setTaskName(id: Int, name: String)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("SELECT * FROM task WHERE project_id = :project_id")
    fun getOneProjectTasks(project_id: Int): Flow<List<TaskEntity>>

    @Query("DELETE FROM task")
    fun deleteAllTasks()

    @Query("UPDATE task SET todo_description = :todo_description WHERE todo_id = :todo_id")
    fun updateDesc(todo_id: Int, todo_description: String?)

    @Query("UPDATE task SET project_id = :project_id WHERE todo_id = :todo_id")
    fun updateTaskRelateProject(todo_id:Int, project_id: Int)

    @Query("UPDATE task SET todo_deadline = :todo_deadline WHERE todo_id = :todo_id")
    fun updateDeadLineTime(todo_id: Int, todo_deadline: OffsetDateTime?)

    @Query("UPDATE task SET todo_deadline_remind = :todo_deadline_remind WHERE todo_id = :todo_id")
    fun updateDeadLineRemind(todo_id: Int, todo_deadline_remind: OffsetDateTime?)

    @Query("UPDATE task SET todo_execute_remind = :todo_execute_remind WHERE todo_id = :todo_id")
    fun updateExecuteRemindTime(todo_id: Int, todo_execute_remind: OffsetDateTime?)

    @Query("UPDATE task SET todo_execute_starttime = :todo_execute_starttime WHERE todo_id = :todo_id")
    fun updateExecuteStartTime(todo_id: Int, todo_execute_starttime: OffsetDateTime?)

    @Query("UPDATE task SET todo_execute_endtime = :todo_execute_endtime WHERE todo_id = :todo_id")
    fun updateExecuteEndTime(todo_id: Int, todo_execute_endtime: OffsetDateTime?)

    @Query("UPDATE task SET todo_priority = :todo_priority WHERE todo_id = :todo_id")
    fun updatePriority(todo_id: Int, todo_priority: TaskPriority)
}