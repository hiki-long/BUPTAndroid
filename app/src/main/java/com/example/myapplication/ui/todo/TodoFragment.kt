package com.example.myapplication.ui.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.example.myapplication.BuildConfig
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.dao.TaskDao
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.Project
import com.example.myapplication.model.TaskState
import com.example.myapplication.ui.adapter.TaskAdapter
import com.example.myapplication.ui.fragment.AddTaskActivity
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.TasksViewModelSimpleFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


@AndroidEntryPoint
class TodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var tasklist: ArrayList<TaskEntity>
    private lateinit var taskDao: TaskDao
    private lateinit var tasksViewModel: TasksViewModelSimple
    private var adapter = TaskAdapter()
    private val defaultProjectName="全部"
    private val defaultProjectId=-1
    private var currentProjectId = defaultProjectId
    private var currentProjectName = defaultProjectName
    private var lastLiveData:LiveData<List<TaskEntity>>?=null
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        todoViewModel =
            ViewModelProvider(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo, container, false)
        if(savedInstanceState!=null){
            currentProjectId=savedInstanceState.getInt("currentProjectId",defaultProjectId)
            currentProjectName= savedInstanceState.getString("currentProjectName",defaultProjectName)
        }

        databaseBinder(TodoListDisplayOptions.initialization)
        //databaseBinder(TodoListDisplayOptions.getOneProjectTask,projectId = currentProjectId,projectName = currentProjectName)

        val bt: FloatingActionButton = root.findViewById(R.id.add)
        bt.setOnClickListener {
            var intent = Intent(requireActivity(), AddTaskActivity::class.java)
            if(currentProjectId==-1){
                intent.putExtra("projectId", 1)
                intent.putExtra("projectName", "收集箱")
            }
            else{
                intent.putExtra("projectId", currentProjectId)
                intent.putExtra("projectName", currentProjectName)
            }
            startActivity(intent)
        }
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).needDrawer(true)
        (activity as MainActivity).replaceAdapterFragment(this)
        postponeEnterTransition()
        recyclerview.layoutManager = LinearLayoutManager(requireActivity())
        recyclerview.adapter = adapter
        recyclerview.doOnPreDraw {
            startPostponedEnterTransition()
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var p = tasklist.removeAt(viewHolder.adapterPosition)
                //mainViewModel.deleteTask(p.todo_id)
                tasksViewModel.deleteTaskOfTaskId(p.todo_id)
                Snackbar.make(
                    requireActivity().findViewById(R.id.fragment_todo),
                    "删除一项task",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("撤销") {
                        tasklist.add(p)
                        tasksViewModel.insertTask(
                                p.todo_create_time,
                                p.todo_state,
                                p.todo_name,
                                p.project_id,
                                p.todo_priority,
                                p.todo_execute_starttime,
                                p.todo_execute_endtime,
                                p.todo_execute_remind,
                                p.todo_deadline,
                                p.todo_deadline_remind,
                                p.todo_description
                        )

                    }.show()
                adapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(recyclerview)

        adapter.taskClickListener = object : TaskAdapter.TaskClickListener {
            override fun updateTask(task: TaskEntity) {
                mainViewModel.updateTask(task)
            }

            override fun getProject(id: Int): Flow<Project?> {
                return mainViewModel.getList(id)
            }
        }
        taskDao = AppDatabase.getDatabase(activity as MainActivity).taskDao()
        tasksViewModel = ViewModelProvider(
            this,
            TasksViewModelSimpleFactory(taskDao)
        ).get(TasksViewModelSimple::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_ordinarylist, menu)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.editlist -> {
//                var dialog = ListDialogCreate()
//                var args = Bundle()
//                args.putString("title", "编辑清单")
//                dialog.arguments = args
//                dialog.show(parentFragmentManager, "listdialog")
//            }
//            R.id.deletelist -> {
//
//            }
            R.id.sort -> {
                val myItems = listOf("清单", "创建日期", "截至日期", "执行时间", "重要级")
                MaterialDialog(requireContext()).show {
                    cornerRadius(16f)
                    title(R.string.sort)
                    listItemsSingleChoice(
                        items = myItems,
                        waitForPositiveButton = false
                    ) { dialog, index, text ->
                            databaseBinder(TodoListDisplayOptions.getTasksInOneOrder,index=index)
//                        mainViewModel.getTasks(index).observe(
//                            viewLifecycleOwner,
//                            {
//                                tasklist = it as ArrayList<TaskEntity>
//                                adapter.submitList(tasklist)
//                                findNavController().navigateUp()
//                            }
//                        )
                        dismiss()
                    }
                    negativeButton(R.string.cancel)
                }
            }
//            R.id.showCompleted -> {
//                activity?.setTitle("已完成")
//                lastLiveData=tasksViewModel.finishedTasksLiveData
//                lastLiveData!!.observe(viewLifecycleOwner, {
//                    tasklist = it as ArrayList<TaskEntity>
//                    adapter.submitList(tasklist)
//                    findNavController().navigateUp()
//                })
//            }
        }
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun databaseBinder(mode:TodoListDisplayOptions, index:Int=-1, projectId: Int=-1, projectName: String="None"){
        if(view!=null){
            lastLiveData?.removeObservers(viewLifecycleOwner)
        }
        when(mode){
            TodoListDisplayOptions.initialization->{
                activity?.setTitle("所有")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData=mainViewModel.getTasks(0)
                lastLiveData!!.observe(
                    viewLifecycleOwner,
                    {
                        tasklist = it as ArrayList<TaskEntity>
                        adapter.submitList(tasklist)
                        findNavController().navigateUp()
                    }
                )
            }
            TodoListDisplayOptions.getTasksInOneOrder-> {
                if (BuildConfig.DEBUG && index == -1) {
                    error("Assertion failed")
                }
                lastLiveData = mainViewModel.getTasks(index)
                lastLiveData!!.observe(
                    viewLifecycleOwner,
                    {
                        tasklist = it as ArrayList<TaskEntity>
                        adapter.submitList(tasklist)
                        findNavController().navigateUp()
                    }
                )
            }
            TodoListDisplayOptions.getOneProjectTask -> {
                if (BuildConfig.DEBUG && !(projectId != -1 && projectName != "None")) {
                    error("Assertion failed")
                }
                activity?.setTitle(projectName)
                activity?.findViewById<DrawerLayout>(R.id.mainDrawerLayout)
                    ?.closeDrawer(GravityCompat.START)
                lastLiveData = mainViewModel.getTasks(5, projectId)
                lastLiveData!!.observe(
                    viewLifecycleOwner,
                    {
                        tasklist = it as ArrayList<TaskEntity>
                        adapter.submitList(tasklist)
                        findNavController().navigateUp()
                    }
                )
                currentProjectId = projectId
                currentProjectName = projectName
                (activity as MainActivity).slideTopCancelSelected()
            }
            TodoListDisplayOptions.filterAll -> {
                activity?.setTitle("所有")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData = tasksViewModel.tasksLiveData
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }
            TodoListDisplayOptions.filterToday -> {
                activity?.setTitle("今天")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData = tasksViewModel.todayTasksLiveData()
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }
            TodoListDisplayOptions.filterImportant -> {
                activity?.setTitle("重要")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData = tasksViewModel.importantTasksLiveData
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }
            TodoListDisplayOptions.filterPlanned -> {
                activity?.setTitle("已计划")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData=tasksViewModel.plannedTasksLiveData
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }
            TodoListDisplayOptions.filterFinished -> {
                activity?.setTitle("已完成")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData=tasksViewModel.finishedTasksLiveData
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }
            TodoListDisplayOptions.filterCollectbox -> {
                activity?.setTitle("收集箱")
                currentProjectId = defaultProjectId
                currentProjectName = defaultProjectName
                lastLiveData=tasksViewModel.collecboxTaksLiveData
                lastLiveData!!.observe(viewLifecycleOwner, {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                })
            }

        }
    }

}
