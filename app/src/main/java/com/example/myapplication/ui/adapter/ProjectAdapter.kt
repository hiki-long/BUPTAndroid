package com.example.myapplication.ui.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.example.myapplication.R
import com.example.myapplication.model.Project
import com.example.myapplication.ui.todo.ListDialogCreate
import com.google.android.material.snackbar.Snackbar

class ProjectAdapter(val context: Context,val dialogfragmentManager: FragmentManager) : ListAdapter<Project, ProjectAdapter.ViewHolder>(MyCallback()) {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val projectName: TextView =view.findViewById(R.id.listName)
        val button_edit=view.findViewById<ImageView>(R.id.button_edit)
        val button_delete=view.findViewById<ImageView>(R.id.button_delete)
    }

    companion object {
        class MyCallback : DiffUtil.ItemCallback<Project>() {
            override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
                return oldItem.project_id==newItem.project_id
            }

            override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
                return false;
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectAdapter.ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.item_listsetting,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectAdapter.ViewHolder, position: Int) {
        val project=getItem(position)
        holder.projectName.text=project.project_name
        holder.button_edit.setOnClickListener {
            var dialog = ListDialogCreate()
            var args = Bundle()
            args.putString("title", "编辑清单")
            dialog.arguments = args
            dialog.show( dialogfragmentManager, "listdialog")
        }
        holder.button_delete.setOnClickListener {
            MaterialDialog(context).show{
                cornerRadius(16f)
                title(R.string.title_delete)
                message(R.string.massagedelete)
                positiveButton(R.string.confirmdelete){

                }
                negativeButton(R.string.cancel)
            }
        }
    }
}