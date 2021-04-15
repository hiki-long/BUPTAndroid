package com.example.myapplication.db

import androidx.room.TypeConverter
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toString(taskState: TaskState?): String? = taskState?.name

    @TypeConverter
    fun toTaskState(name: String?): TaskState =
        name?.let { enumValueOf<TaskState>(it) } ?: TaskState.DOING

    @TypeConverter
    fun toString(taskpriority: TaskPriority?): String? = taskpriority?.name

    @TypeConverter
    fun toTaskPriority(name: String?): TaskPriority =
        name?.let { enumValueOf<TaskPriority>(it) } ?: TaskPriority.COMMON
}