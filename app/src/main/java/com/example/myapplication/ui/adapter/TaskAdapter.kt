package com.example.myapplication.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.Project
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.ui.todo.TodoItemDetailActivity
import com.example.myapplication.ui.uti.UtiFunc
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.TasksViewModelSimpleFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

class TaskAdapter(val activity: AppCompatActivity) : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(MyCallback()) {
    lateinit var taskClickListener: TaskClickListener
    private lateinit var tasksViewModelSimple: TasksViewModelSimple
    init {
        tasksViewModelSimple= ViewModelProvider(
            activity,
            TasksViewModelSimpleFactory(AppDatabase.getDatabase(activity).taskDao())
        )
            .get(TasksViewModelSimple::class.java)
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var todoName: TextView =view.findViewById(R.id.text_todoName)
        var execute_time=view.findViewById<TextView>(R.id.text_execute_time)
        var deadline=view.findViewById<TextView>(R.id.text__deadline)
        var img_alert=view.findViewById<ImageView>(R.id.img_alert)
        var img_descrip=view.findViewById<ImageView>(R.id.img_descrip)
        var img_importance=view.findViewById<ImageView>(R.id.img_importance)
        var checkBox=view.findViewById<CheckBox>(R.id.checkBox)
       // var textview_list=view.findViewById<TextView>(R.id.textview_list)
        lateinit var task:TaskEntity
    }

    companion object {
        class MyCallback : DiffUtil.ItemCallback<TaskEntity>() {
            override fun areItemsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return oldItem.todo_id==newItem.todo_id
            }

            override fun areContentsTheSame(oldItem: TaskEntity, newItem: TaskEntity): Boolean {
                return false;
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false)
        var holder=ViewHolder(view)

        holder.img_importance.setOnClickListener {
            if(holder.task.todo_priority==TaskPriority.EMERGENCY){
                holder.img_importance.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.task.todo_priority=TaskPriority.COMMON
                tasksViewModelSimple.updateTaskPriority(holder.task.todo_id,TaskPriority.COMMON)
            }
            else{
                holder.img_importance.setImageResource(R.drawable.ic_baseline_star_40)
                holder.task.todo_priority=TaskPriority.EMERGENCY
                tasksViewModelSimple.updateTaskPriority(holder.task.todo_id,TaskPriority.EMERGENCY)
            }
            taskClickListener.updateTask(holder.task)
        }

        holder.checkBox.setOnClickListener{
            if(holder.checkBox.isChecked) {
                holder.task.todo_state = TaskState.DONE
                tasksViewModelSimple.setTaskState(holder.task.todo_id,TaskState.DONE)
            }
            else {
                holder.task.todo_state = TaskState.DOING
                tasksViewModelSimple.setTaskState(holder.task.todo_id,TaskState.DOING)
            }
            taskClickListener.updateTask(holder.task)
        }
        return holder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        val task=getItem(position)
        holder.task=task

        holder.todoName.text=task.todo_name

        holder.itemView.setOnClickListener {
            TodoItemDetailActivity.actionStart(holder.itemView.context,task.todo_id)
        }

        if(task.todo_execute_starttime!=null)
            holder.execute_time.text=UtiFunc.Time2String(task.todo_execute_starttime!!)
        else
            holder.execute_time.visibility=View.GONE

        if(task.todo_deadline!=null)
            holder.deadline.text=UtiFunc.Time2String(task.todo_deadline!!)
        else
            holder.deadline.visibility=View.GONE

        holder.checkBox.isChecked=task.todo_state==TaskState.DONE

        if((task.todo_execute_remind==null || task.todo_execute_remind!! < OffsetDateTime.now())&&(task.todo_deadline_remind==null || task.todo_deadline_remind!! < OffsetDateTime.now()))
            holder.img_alert.visibility=View.GONE
        else
            holder.img_alert.visibility=View.VISIBLE

        if(task.todo_description==null||task.todo_description!!.isEmpty())
            holder.img_descrip.visibility=View.GONE
        else
            holder.img_descrip.visibility=View.VISIBLE

        if(task.todo_priority == TaskPriority.COMMON) {
            holder.img_importance.setImageResource(R.drawable.ic_baseline_star_border_24)
        }
        else {
            holder.img_importance.setImageResource(R.drawable.ic_baseline_star_40)
        }
        //????????????id??????????????????name
    }

    interface TaskClickListener {
        fun updateTask(task:TaskEntity)
        fun getProject(id: Int): Flow<Project?>
    }
}
