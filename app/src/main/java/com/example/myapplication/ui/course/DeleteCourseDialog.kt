package com.example.myapplication.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class DeleteCourseDialog : DialogFragment() {
    private lateinit var viewmodel: CourseViewModel
    private var datasource: ArrayList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null) {
            datasource = requireArguments().getStringArrayList("data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view =  inflater.inflate(R.layout.fragment_delete, container, false) as View
        viewmodel = ViewModelProvider(requireActivity()).get(CourseViewModel::class.java)
        val adapter = datasource?.let {
            DeleteCourseAdapter(viewmodel, it)
        }
        val layoutManager = LinearLayoutManager(view.context)
        val recycleview = view.findViewById<RecyclerView>(R.id.show_delete_cousre)
        recycleview.adapter = adapter
        recycleview.layoutManager = layoutManager
        val cancel = view.findViewById<Button>(R.id.edit_cancel)
        cancel.setOnClickListener {
            viewmodel.deletedone.value = false
            dismiss()
        }
        val confirm = view.findViewById<Button>(R.id.edit_confirm)
        confirm.setOnClickListener {
            viewmodel.deletedone.value = true
            dismiss()
        }
        return view
    }
}