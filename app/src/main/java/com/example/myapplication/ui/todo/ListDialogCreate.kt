package com.example.myapplication.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch


class ListDialogCreate : DialogFragment() {
    private var thistitle: String? = null
    private lateinit var editTitle: TextView
    private var cancelbutton: Button? = null
    private var confirmbutton: Button? = null
    private var listcolor: String? = null
    private var showbutton: Button? = null
    private var lastColor: Int? = null
    private lateinit var linearlayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thistitle = savedInstanceState?.getString("title")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.listdialog, container, false) as View
        confirmbutton = view.findViewById(R.id.confirmcolorbutton)
        cancelbutton = view.findViewById(R.id.cancelcolorbutton)
        cancelbutton?.setOnClickListener {
            dismiss()
        }
        confirmbutton?.setOnClickListener {
            //这里之后要加入对数据库的操作
            dismiss()
        }
        showbutton = view.findViewById(R.id.color_show)
        editTitle = view.findViewById(R.id.edit_title)
        linearlayout = view.findViewById(R.id.showcolorline)
        linearlayout.setOnClickListener{
            MaterialColorPickerDialog
                    .Builder(view.context)
                    .setTitle("清单颜色选择")
                    .setColorShape(ColorShape.SQAURE)
                    .setColorSwatch(ColorSwatch._300)
                    .setDefaultColor("")
                    .setColorListener { color, colorHex ->
                        listcolor = colorHex
                        showbutton?.setBackgroundColor(color)
                    }
                    .show()
        }
        setTitle(thistitle)
        return view
    }

    fun setTitle(title: String?){
        if(title != null)
            editTitle.text = title
    }

}