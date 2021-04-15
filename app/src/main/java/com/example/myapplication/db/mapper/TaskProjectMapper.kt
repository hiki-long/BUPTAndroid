package com.example.myapplication.db.mapper

import com.example.myapplication.db.view.TaskProjectView
import com.example.myapplication.model.TaskProject

object TaskProjectMapper {
    fun TaskProjectView?.toDomain(): TaskProject? = this?.let{
        TaskProject(
            project_id = it.task.project_id,
            project_name = it.projectName,
            project_color = it.projectColor,
            task_id = it.task.task_id,
            todo_create_time = it.task.todo_create_time,
            todo_state = it.task.todo_state,
            todo_name = it.task.todo_name,
            todo_priority = it.task.todo_priority,
            todo_execute_starttime = it.task.todo_execute_starttime,
            todo_execute_endtime = it.task.todo_execute_endtime,
            todo_execute_remind = it.task.todo_execute_remind,
            todo_deadline = it.task.todo_deadline,
            todo_deadline_remind = it.task.todo_deadline_remind,
            todo_description = it.task.todo_description
        )
    }
}