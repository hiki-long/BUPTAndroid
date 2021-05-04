package com.example.myapplication.usecase

import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import java.time.OffsetDateTime

//插入todo
class InsertTodoCase (private val taskRepository: TaskRepository) {
    suspend operator fun invoke(
        todo_create_time: OffsetDateTime,
        todo_state: TaskState,
        todo_name: String,
        project_id: Int,
        todo_priority: TaskPriority,
        todo_execute_starttime: OffsetDateTime,
        todo_execute_endtime: OffsetDateTime,
        todo_execute_remind: OffsetDateTime,
        todo_deadline: OffsetDateTime,
        todo_deadline_remind: OffsetDateTime,
        todo_description: String?
    ): Long? =
            taskRepository.insert(
                    Task(
                            todo_create_time =  todo_create_time,
                            todo_state = todo_state,
                            todo_name = todo_name,
                            project_id = project_id,
                            todo_priority = todo_priority,
                            todo_execute_starttime = todo_execute_starttime,
                            todo_execute_endtime = todo_execute_endtime,
                            todo_execute_remind = todo_execute_remind,
                            todo_deadline = todo_deadline,
                            todo_deadline_remind = todo_deadline_remind,
                            todo_description = todo_description
                    )
            )

}