/*
 * Copyright 2021 Sergio Belda Galbis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication.usecase

import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.db.repository.TaskRepository
import com.example.myapplication.model.Task
import kotlinx.coroutines.flow.Flow

class GetTasksCase(
    private val taskRepository: TaskRepository
) {

    /**
     * Gets a task by it id.
     *
     * @param id Task id.
     */
    operator fun invoke(order:Int?): Flow<List<TaskEntity>> =
        if(order==null||order==1)
            taskRepository.getTasks()
        else if(order==0)
            taskRepository.getTasksOrderByProject()
        else if(order==2)
            taskRepository.getTasksOrderByDeadline()
        else if(order==3)
            taskRepository.getTasksOrderByExecuteTime()
        else if(order==4)
            taskRepository.getTasksOrderByPriority()
        else
            taskRepository.getTasks()
}
