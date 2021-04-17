package com.example.myapplication.ui.todo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TodoFragment : Fragment() {

    private lateinit var todoViewModel: TodoViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todoViewModel =
            ViewModelProvider(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo, container, false)
        val textView: TextView = root.findViewById(R.id.text_todo)
        todoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val bt: FloatingActionButton = root.findViewById(R.id.add)
        bt.setOnClickListener {
//            var dialog = ListDialogCreate()
//            dialog.show(parentFragmentManager,"listdialog")
            MaterialColorPickerDialog
                .Builder((activity as MainActivity?)!!)        					// Pass Activity Instance
                .setTitle("Pick Theme")           		// Default "Choose Color"
                .setColorShape(ColorShape.SQAURE)   	// Default ColorShape.CIRCLE
                .setColorSwatch(ColorSwatch._300)   	// Default ColorSwatch._500
                .setDefaultColor("#F5F5F5") 		// Pass Default Color
                .setColorListener { color, colorHex ->
                    // Handle Color Selection
                }
                .show()
        }
        return root
    }
}