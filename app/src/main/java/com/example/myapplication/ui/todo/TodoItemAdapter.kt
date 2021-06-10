package com.example.myapplication.ui.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class TodoItemAdapter(val itemList:List<listItem>,val fragment:TodoFragment):RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val itemName:TextView=view.findViewById(R.id.todo_item_list_item_view_name)
        val todoNum:TextView=view.findViewById(R.id.todo_item_list_item_view_num)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.todo_item_list_item_view,parent,false)
        val viewHolder=ViewHolder(view)
        return viewHolder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=itemList[position]
        holder.itemName.text=item.itemName
        holder.todoNum.text=item.todoNum.toString()
        holder.itemView.setOnClickListener {
            fragment.databaseBinder(TodoListDisplayOptions.getOneProjectTask,projectId = item.projectId,projectName = item.itemName)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}