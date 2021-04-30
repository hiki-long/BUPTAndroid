package com.example.myapplication.ui.todo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import dagger.hilt.android.AndroidEntryPoint
import java.time.OffsetDateTime

@AndroidEntryPoint
class ListDialogCreate : DialogFragment() {
    private var thistitle: String? = null
    private lateinit var editTitle: TextView
    private var cancelbutton: Button? = null
    private var confirmbutton: Button? = null
    private var listcolor: String? = null
    private var showbutton: Button? = null
    private var lastColor: Int? = null
    private lateinit var linearlayout: LinearLayout
    private var testview : View? = null
    private val testViewModel by viewModels<TestViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            thistitle = arguments?.getString("title")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.listdialog, container, false) as View
        testview = view
        confirmbutton = view.findViewById(R.id.confirmcolorbutton)
        cancelbutton = view.findViewById(R.id.cancelcolorbutton)
        cancelbutton?.setOnClickListener {
            dismiss()
        }
        confirmbutton?.setOnClickListener {
            //这里之后要加入对数据库的操作
            insertTodo()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertTodo() {

        val name = testview?.findViewById(R.id.list_name) as EditText
        testViewModel.InsertTask(OffsetDateTime.now(), TaskState.DONE,"sdf",1, TaskPriority.COMMON, OffsetDateTime.now(),OffsetDateTime.now(),OffsetDateTime.now(),OffsetDateTime.now(),OffsetDateTime.now(),"none").observe(
                viewLifecycleOwner,
                {
                    findNavController().navigateUp()
                }
        )
    }

    fun setTitle(title: String?){
        if(title != null)
            editTitle.text = title
    }

}