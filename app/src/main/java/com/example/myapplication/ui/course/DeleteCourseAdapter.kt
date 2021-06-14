package com.example.myapplication.ui.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DeleteCourseAdapter(
    val viewmodel: CourseViewModel,
    val mDatas: ArrayList<String>
) : RecyclerView.Adapter<DeleteCourseAdapter.myViewHodler>() {

    class myViewHodler(view: View): RecyclerView.ViewHolder(view) {
        val coursename = view.findViewById<TextView>(R.id.course_name)
        val iselect = view.findViewById<RadioButton>(R.id.is_course_todelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHodler {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_delete_onerow, parent, false)
        return myViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    override fun onBindViewHolder(holder: myViewHodler, position: Int) {
        val item = mDatas[position]
        holder.coursename.text = item
        holder.iselect.setOnClickListener {
            var temp = viewmodel.deletelist.value
            if(temp == null) {
                temp = mutableSetOf()
            }
            if(holder.iselect.isChecked) {
                temp.add(position)
            } else {
                temp.remove(position)
            }
            viewmodel.deletelist.value = temp
        }
    }
}