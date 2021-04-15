package com.example.myapplication.db

import android.content.Context
import androidx.room.*
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.dao.TaskProjectViewDao
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.db.view.TaskProjectView
import java.security.AccessControlContext

@Database(
    entities = [ProjectEntity::class, TaskEntity::class],
    views = [TaskProjectView::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun projectDao(): ProjectDao

    abstract fun taskDao(): TaskDao

    abstract fun taskProjectViewDao(): TaskProjectViewDao

//    companion object{
//        //单例模式,只有一个database即可
//        @Volatile
//        private var instance: AppDatabase?= null
//        private val LOCK = Any()
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
//            instance ?: createDatabase(context).also {instance = it}
//
//        }
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                "app.db"
//            )
//            .createFromAsset("database/AppDatabase.db")
//            .build()
//    }
}