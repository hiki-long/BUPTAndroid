package com.example.myapplication.ui.todo

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.db.entity.TaskEntity

enum class TodoListDisplayOptions {
    initialization,
    getTasksInOneOrder,
    getOneProjectTask,
    filterAll,
    filterToday,
    filterImportant,
    filterPlanned,
    filterFinished,
}

