package com.example.myapplication.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.ui.todo.TodoItemDetailActivity

class TaskAdapter() : ListAdapter<TaskEntity, TaskAdapter.ViewHolder>(MyCallback()) {
    private var importanct=false
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var todoName: TextView =view.findViewById(R.id.text_todoName)
        var execute_time=view.findViewById<TextView>(R.id.text_execute_time)
        var deadline=view.findViewById<TextView>(R.id.text__deadline)
        var img_alert=view.findViewById<ImageView>(R.id.img_alert)
        var img_descrip=view.findViewById<ImageView>(R.id.img_descrip)
        var img_importance=view.findViewById<ImageView>(R.id.img_importance)
        //var text_list=view.findViewById<TextView>(R.id.textview_list)
        var checkBox=view.findViewById<CheckBox>(R.id.checkBox)
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

        holder.itemView.setOnClickListener {
            //TODO 点击跳转详情页

            TodoItemDetailActivity.actionStart(parent.context)
        }

        holder.img_importance.setOnClickListener {
            if(importanct){
                holder.img_importance.setImageResource(R.drawable.ic_baseline_star_border_24)
                importanct=false
            }
            else{
                holder.img_importance.setImageResource(R.drawable.ic_baseline_star_24)
                importanct=true
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: TaskAdapter.ViewHolder, position: Int) {
        val task=getItem(position)
        holder.todoName.text=task.todo_name
        holder.execute_time.text=task.todo_execute_starttime.toString()
        holder.deadline.text=task.todo_deadline.toString()
        holder.checkBox.isChecked=task.todo_state==TaskState.DONE
        if(task.todo_execute_remind==null&&task.todo_deadline_remind==null)
            holder.img_alert.visibility=View.GONE
        else
            holder.img_alert.visibility=View.VISIBLE
        if(task.todo_description==null)
            holder.img_descrip.visibility=View.GONE
        else
            holder.img_descrip.visibility=View.VISIBLE
        if(task.todo_priority == TaskPriority.COMMON) {
            holder.img_importance.setImageResource(R.drawable.ic_baseline_star_border_24)
            importanct=false
        }
        else {
            holder.img_importance.setImageResource(R.drawable.ic_baseline_star_24)
            importanct=true
        }
        //根据清单id查表得到清单name
    }
}