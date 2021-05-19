package com.example.myapplication.ui.todo

import androidx.lifecycle.*
import com.example.myapplication.model.Project
import com.example.myapplication.model.Task
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.usecase.GetListsCase
import com.example.myapplication.usecase.InsertListCase
import com.example.myapplication.usecase.InsertTodoCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
        private val setTodo: InsertTodoCase,
        private val setList: InsertListCase,
        getLists: GetListsCase,
) :ViewModel() {

    val lists: LiveData<List<Project?>> = getLists().asLiveData()

    fun InsertList(
            project_name: String,
            project_color: Int?,
            tasks: List<Task?>
    )
            = liveData {
        emit(setList.invoke(project_name, project_color,tasks))
    }

    fun InsertTask(
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
            todo_description: String?)
            =   liveData {
        emit(setTodo.invoke(todo_create_time,todo_state,todo_name,project_id,todo_priority,todo_execute_starttime,todo_execute_endtime,todo_execute_remind,todo_deadline,todo_deadline_remind,todo_description))
    }

}