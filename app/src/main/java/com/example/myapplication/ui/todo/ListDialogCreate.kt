package com.example.myapplication.ui.todo

import android.os.Bundle
import android.service.quicksettings.Tile
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import org.w3c.dom.Text

class ListDialogCreate : DialogFragment() {
    private var thistitle: String? = null
    private lateinit var editTitle: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thistitle = savedInstanceState?.getString("title")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.listdialog, container, false) as View
        editTitle = view.findViewById(R.id.edit_title)
        setTitle(thistitle)
        return view
    }

    fun setTitle(title: String?){
        if(title != null)
            editTitle.text = title
    }

}