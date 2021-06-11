package com.example.myapplication.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        private val insertTodoCase: InsertTodoCase,
        private val getTasksCase: GetTasksCase,
        private val deleteTaskCase: DeleteTodoCase,
        private val updateTaskCase: UpdateTaskCase,
        private val getListsCase:GetListsCase
) : ViewModel() {
    fun insertTask(todo_create_time: OffsetDateTime,
                   todo_state: TaskState,
                   todo_name: String,
                   project_id: Int,
                   todo_priority: TaskPriority,
                   todo_execute_starttime: OffsetDateTime?,
                   todo_execute_endtime: OffsetDateTime?,
                   todo_execute_remind: OffsetDateTime?,
                   todo_deadline: OffsetDateTime?,
                   todo_deadline_remind: OffsetDateTime?,
                   todo_description: String?) =
            liveData {
                emit(insertTodoCase.invoke(todo_create_time,todo_state,todo_name, project_id,todo_priority,todo_execute_starttime,
                        todo_execute_endtime,todo_execute_remind,todo_deadline,todo_deadline_remind,todo_description))
            }

    fun getTasks(order:Int?,project_id: Int=0): LiveData<List<TaskEntity>> = getTasksCase.invoke(order,project_id).asLiveData()

    fun deleteTask(id: Int) = viewModelScope.launch {
        deleteTaskCase.invoke(id)
    }

    fun updateTask(task:TaskEntity) = viewModelScope.launch {
        updateTaskCase.invoke(task)
    }
}