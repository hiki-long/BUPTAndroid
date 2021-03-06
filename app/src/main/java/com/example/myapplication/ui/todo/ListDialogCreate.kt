package com.example.myapplication.ui.todo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.viewmodel.ProjectsViewModelSimple
import com.example.myapplication.viewmodelFactory.ProjectsViewModelSimpleFactory
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
    private val testViewModel by viewModels<TodoViewModel>()
    private lateinit var projectsViewModelSimple: ProjectsViewModelSimple
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
        val thisContext=context
        if(thisContext!=null){
            projectsViewModelSimple= ViewModelProvider(
                this,
                ProjectsViewModelSimpleFactory(AppDatabase.getDatabase(thisContext).projectDao())
            )
                .get(ProjectsViewModelSimple::class.java)
        }

        confirmbutton = view.findViewById(R.id.confirmcolorbutton)
        cancelbutton = view.findViewById(R.id.cancelcolorbutton)
        cancelbutton?.setOnClickListener {
            dismiss()
        }
        confirmbutton?.setOnClickListener {
            if(thistitle=="????????????"){
                insertList()
            }
            else{
                updateList()
            }

            dismiss()
        }
        showbutton = view.findViewById(R.id.color_show)
        editTitle = view.findViewById(R.id.edit_title)
        linearlayout = view.findViewById(R.id.showcolorline)
        linearlayout.setOnClickListener{
            MaterialColorPickerDialog
                    .Builder(view.context)
                    .setTitle("??????????????????")
                    .setColorShape(ColorShape.SQAURE)
                    .setColorSwatch(ColorSwatch._300)
                    .setDefaultColor("")
                    .setColorListener { color, colorHex ->
                        listcolor = colorHex
                        lastColor = color
                        showbutton?.setBackgroundColor(color)
                    }
                    .show()
        }
        setTitle(thistitle)
        return view
    }

    private fun insertList() {
        val name = testview?.findViewById(R.id.list_name) as EditText
        val result=projectsViewModelSimple.insertAProject(name.text.toString(),lastColor)
        //findNavController().navigateUp()
        if(result.compareTo(-1)==0){
            Toast.makeText(activity,"?????????????????????????????????",Toast.LENGTH_SHORT).show()
        }
//        testViewModel.InsertList(name.text.toString(),lastColor, emptyList()).observe(
//                viewLifecycleOwner,
//                {
//                    findNavController().navigateUp()
//
//                    if(it?.compareTo(-1) == 0)
//                    {
//                        Toast.makeText(activity,"?????????????????????????????????",Toast.LENGTH_SHORT).show()
//                    }
//                }
//        )
    }

    private fun updateList(){
        val name = testview?.findViewById(R.id.list_name) as EditText
        val projectId=arguments?.getInt("projectId")
        if(projectId!=null){
            val result=projectsViewModelSimple.updateAProject(projectId,name.text.toString(),lastColor)
            //findNavController().navigateUp()
            if(result==false){
                Toast.makeText(activity,"?????????????????????????????????",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(activity,"?????????????????????????????????",Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertfTodo() {
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