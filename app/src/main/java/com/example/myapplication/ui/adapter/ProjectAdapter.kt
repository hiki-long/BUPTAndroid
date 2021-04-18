package com.example.myapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.model.Project

class ProjectAdapter(val context: Context, val projectList:List<Project>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val projectName: TextView =view.findViewById(R.id.listName)
        val button_edit=view.findViewById<ImageButton>(R.id.button_edit)
        val button_delete=view.findViewById<ImageButton>(R.id.button_delete)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.item_listsetting,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ViewHolder, position: Int) {
        val project=projectList[position]
        holder.projectName.text=project.project_name
        holder.button_edit.setOnClickListener {

        }
        holder.button_delete.setOnClickListener {
            MaterialDialog(context).show{
                cornerRadius(16f)
                title(R.string.title_delete)
                message(R.string.massagedelete)
                positiveButton(R.string.confirmdelete)
                negativeButton(R.string.cancel)
            }
        }
    }

    override fun getItemCount() = projectList.size
}