package com.example.myapplication.db.repository

import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.db.mapper.TaskMapper.toDomain
import com.example.myapplication.db.mapper.TaskMapper.toEntity
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime

class TaskRepository(private val taskDao: TaskDao) {
    fun getTask(id: Int): Flow<Task?> = taskDao.getTask(id).map { it.toDomain() }

    fun getTaskInProject(project_id: Int): Flow<List<Task?>> = taskDao.getOneProjectTasks(project_id).map { list -> list.map { it.toDomain() } }

    fun getTasks(): Flow<List<TaskEntity>> =taskDao.getTasks()

    fun getTasksOfAProject(project_id: Int):Flow<List<TaskEntity>> = taskDao.getOneProjectTasks(project_id)

    fun getTasksOrderByProject(): Flow<List<TaskEntity>> =taskDao.getTasksOrderByProject()

    fun getTasksOrderByDeadline(): Flow<List<TaskEntity>> =taskDao.getTasksOrderByDeadline()

    fun getTasksOrderByExecuteTime(): Flow<List<TaskEntity>> =taskDao.getTasksOrderByExecuteTime()

    fun getTasksOrderByPriority(): Flow<List<TaskEntity>> =taskDao.getTasksOrderByPriority()

    suspend fun deleteTask(id: Int) = taskDao.deleteTask(id)

    suspend fun insert(task: Task): Long? = task.toEntity()?.let { taskDao.insertTask(it) }

    suspend fun setTaskState(id: Int, state: TaskState) = taskDao.setTodoState(id, state)

    suspend fun updateTask(task: Task) = task.toEntity()?.let { taskDao.updateTask(it) }

    suspend fun updateTaskEntity(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteAllTasks() = taskDao.deleteAllTasks()

    suspend fun setTaskPriority(todo_id: Int, priority: TaskPriority) = taskDao.updatePriority(todo_id, priority)

    suspend fun setTaskDeadLine(todo_id: Int, dead_line: OffsetDateTime?) = taskDao.updateDeadLineTime(todo_id, dead_line)

    suspend fun setTaskDeadLineRemind(todo_id: Int, dead_line_remind: OffsetDateTime?) = taskDao.updateDeadLineRemind(todo_id, dead_line_remind)

    suspend fun setTaskExecuteRemind(todo_id: Int, execute_remind: OffsetDateTime?) = taskDao.updateExecuteRemindTime(todo_id, execute_remind)

    suspend fun setTaskExecuteBegin(todo_id: Int, execute_begin_time: OffsetDateTime?) = taskDao.updateExecuteStartTime(todo_id, execute_begin_time)

    suspend fun setTaskExecuteEnd(todo_id: Int, execute_end_time: OffsetDateTime?) = taskDao.updateExecuteEndTime(todo_id, execute_end_time)

    suspend fun setTaskDesc(todo_id: Int, description: String?) = taskDao.updateDesc(todo_id, description)

    suspend fun setTaskToAnotherProject(todo_id: Int, project_id: Int) = taskDao.updateTaskRelateProject(todo_id, project_id)
}