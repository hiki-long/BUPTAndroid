package com.example.myapplication.ui.todo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.db.AppDatabase
import com.example.myapplication.db.dao.ProjectDao
import com.example.myapplication.db.entity.ProjectEntity
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.ui.fragment.AddTaskViewModel
import com.example.myapplication.ui.fragment.AlarmService
import com.example.myapplication.ui.uti.UtiFunc
import com.example.myapplication.viewmodel.MainViewModel
import com.example.myapplication.viewmodel.ProjectsViewModelSimple
import com.example.myapplication.viewmodel.TasksViewModelSimple
import com.example.myapplication.viewmodelFactory.ProjectsViewModelSimpleFactory
import com.example.myapplication.viewmodelFactory.TasksViewModelSimpleFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_todo_item_detail.*
import kotlinx.android.synthetic.main.activity_todo_item_detail.imageView2
import kotlinx.android.synthetic.main.activity_todo_item_detail.imageView3
import kotlinx.android.synthetic.main.activity_todo_item_detail.textView3
import kotlinx.android.synthetic.main.activity_todo_item_detail.textView4
import kotlinx.android.synthetic.main.activity_todo_item_detail.text__deadline
import kotlinx.android.synthetic.main.activity_todo_item_detail.text_description
import kotlinx.android.synthetic.main.activity_todo_item_detail.text_execute_time
import kotlinx.android.synthetic.main.activity_todo_item_detail.todo_end_time
import kotlinx.android.synthetic.main.activity_todo_item_detail.todo_importance
import java.time.OffsetDateTime
import java.util.ArrayList

@AndroidEntryPoint
class TodoItemDetailActivity : AppCompatActivity(), DaySelectDialogCreate.OnListener, TimeBarDialogCreate.OnListener{
    //目前有显示了多少个提醒
    var noticeNum = 0
    var id: Int = 0
    private lateinit var taskItem: TaskEntity
    private lateinit var addtaskViewModel: AddTaskViewModel
    private lateinit var projectsViewModelSimple: ProjectsViewModelSimple
    private lateinit var projectDao: ProjectDao
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var tasksViewModelSimple: TasksViewModelSimple

    companion object {
        fun actionStart(context: Context?, taskId: Int) {
            val intent = Intent(context, TodoItemDetailActivity::class.java)
            intent.putExtra("taskId", taskId)
            context?.startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = intent.getIntExtra("taskId", -1)
        if (BuildConfig.DEBUG && id == -1) {
            error("Assertion failed")
        }
        setContentView(R.layout.activity_todo_item_detail)
        addtaskViewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)
        projectDao=AppDatabase.getDatabase(this).projectDao()
        tasksViewModelSimple = ViewModelProvider(
            this,
            TasksViewModelSimpleFactory(AppDatabase.getDatabase(this).taskDao())
        ).get(TasksViewModelSimple::class.java)
        tasksViewModelSimple.getTaskLiveDataOfTaskId(id).observe(this, {
            taskItem=it
            initView()
        })



    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun initView() {
        setSupportActionBar(todo_item_detail_toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        todo_item_detail_toolbar_down_pull.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.todo_item_detail_toobar_menu, popupMenu.menu)
            popupMenu.show()
        }
        todo_item_detail_toolbar_title.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.todo_item_detail_toobar_menu, popupMenu.menu)
            popupMenu.show()
        }

        todo_item_detail_checkBox.text=taskItem.todo_name
        todo_item_detail_checkBox.isChecked = taskItem.todo_state==TaskState.DONE
        if(taskItem.todo_priority==TaskPriority.EMERGENCY)
        todo_importance.setImageResource(R.drawable.ic_baseline_star_32)
        else
            todo_importance.setImageResource(R.drawable.ic_baseline_star_border_32)

        text_description.setText(taskItem.todo_description)



        val ddlTime=taskItem.todo_deadline
        if(ddlTime!=null){
            text__deadline.text = UtiFunc.Time2String(ddlTime)+" 到期"
            imageView6.visibility = View.VISIBLE
        }
        else{
            text__deadline.text=getString(R.string.todo_item_detail_add_ddl)
            imageView6.visibility=View.GONE
        }

        val executeStartTime=taskItem.todo_execute_starttime
        val executeEndTime=taskItem.todo_execute_endtime
        if(executeStartTime!=null && executeEndTime!=null){
            text_execute_time.text = UtiFunc.Time2String(executeStartTime)+" 至\n"+UtiFunc.Time2String(executeEndTime)+" 执行"
            imageView7.visibility = View.VISIBLE
        }
        else{
            text_execute_time.text=getString(R.string.todo_item_detail_add_execute_time)
            imageView7.visibility=View.GONE
        }

        val ddlNotification=taskItem.todo_deadline_remind

        if(ddlNotification!=null){
            textView3.text="到期提醒:"+UtiFunc.Time2String(ddlNotification)
            imageView3.visibility=View.VISIBLE
        }
        else{
            textView3.text="添加截止时间提醒"
            imageView3.visibility=View.GONE
        }

        val executeNotification=taskItem.todo_execute_remind
        if(executeNotification!=null){
            textView4.text="执行提醒:"+UtiFunc.Time2String(executeNotification)
            imageView2.visibility=View.VISIBLE
        }
        else{
            textView4.text="添加执行时间提醒"
            imageView2.visibility=View.GONE
        }

        val createTime=taskItem.todo_create_time
        todo_item_detail_create_time.text="创建于："+ UtiFunc.Time2String(createTime)

        imageView3.setOnClickListener {
            textView3.text = "添加截止时间提醒"
            imageView3.visibility = View.GONE
            taskItem.todo_deadline_remind=null

        }
        imageView2.setOnClickListener {
            textView4.text ="添加执行时间提醒"
            imageView2.visibility = View.GONE
            taskItem.todo_execute_remind=null
        }


        imageView6.setOnClickListener {
            text__deadline.text="添加截止时间"
            taskItem.todo_deadline=null
            imageView6.visibility = View.GONE
        }

        imageView7.setOnClickListener {
            text_execute_time.text="添加执行时间"
            taskItem.todo_execute_starttime=null
            taskItem.todo_execute_endtime=null
            imageView7.visibility=View.GONE
        }

        todo_item_detail_delete.setOnClickListener {
            //For test
            Toast.makeText(this, "已删除此事例", Toast.LENGTH_SHORT).show()
            mainViewModel.deleteTask(id)
            finish()
        }

        todo_end_time.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title", "设置截止时间")
            args.putInt("mode", 1)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "timeselect")
        }

        todo_detail_execute_time.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title", "设置执行时间")
            args.putInt("mode", 2)
            dialog.arguments = args
            dialog.show(this.supportFragmentManager, "timeselect")
        }

        //下面应该是根据数据库表来动态生成menu
        val popupMenu2 = androidx.appcompat.widget.PopupMenu(
                this,
                todo_detail_project_type
        )
        projectsViewModelSimple=ViewModelProvider(this, ProjectsViewModelSimpleFactory(projectDao)).get(ProjectsViewModelSimple::class.java)
        projectsViewModelSimple.projectsListLiveData.observe(this, {
            val projectList=it as ArrayList<ProjectEntity>
            if(!projectList.isEmpty()){
                var choosedProject: ProjectEntity = projectList.get(0)
                if(taskItem.project_id!=-1) {
                    choosedProject= projectList.find{it?.project_id==taskItem.project_id }!!
                }
                todo_project_text.setText(choosedProject.project_name)
                var index=0
                if (!projectList.isEmpty()) {
                    for(aProject in projectList){
                        popupMenu2.menu.add(Menu.NONE, index, index, aProject?.project_name)
                        index+=1
                    }
                }
                popupMenu2.setOnMenuItemClickListener (fun(it: MenuItem): Boolean{
                    choosedProject=projectList.get(it.itemId)
                    taskItem.project_id=choosedProject.project_id
                    todo_project_text.setText(choosedProject?.project_name)
                    return true
                })
            }
        })
        todo_detail_project_type.setOnClickListener {
            popupMenu2.show()
        }

        todo_detail_remind.setOnClickListener {
            val temp = Bundle()
            temp.putInt("mode", 3)
            val timedialog = TimeBarDialogCreate()
            timedialog.arguments = temp
            timedialog.show(supportFragmentManager, "ShowTimeBar")
        }

        detail_executeRemind.setOnClickListener {
            val temp = Bundle()
            temp.putInt("mode", 4)
            val timedialog = TimeBarDialogCreate()
            timedialog.arguments = temp
            timedialog.show(supportFragmentManager, "ShowTimeBar")
        }

        todo_item_detail_checkBox.setOnClickListener{
            if(todo_item_detail_checkBox.isChecked){
                taskItem.todo_state=TaskState.DONE
            }
            else {
                taskItem.todo_state=TaskState.DOING
            }
        }

        todo_importance.setOnClickListener {
            if(taskItem.todo_priority==TaskPriority.EMERGENCY) {
                taskItem.todo_priority = TaskPriority.COMMON
                todo_importance.setImageResource(R.drawable.ic_baseline_star_border_32)
            }
            else{
                taskItem.todo_priority=TaskPriority.EMERGENCY
                todo_importance.setImageResource(R.drawable.ic_baseline_star_32)
            }
        }


        floatingActionButton.setOnClickListener {
            taskItem.todo_description=text_description.text.toString()
            mainViewModel.updateTask(taskItem)
            if(taskItem.todo_deadline_remind!=null){
                if(taskItem.todo_deadline_remind!! > OffsetDateTime.now())
                    AlarmService.addNotification(this,taskItem.todo_deadline_remind.toString() , "deadline", text_description.text.toString()+"任务还未完成哦！")
            }
            if(taskItem.todo_execute_remind!=null){
                if(taskItem.todo_execute_remind!! > OffsetDateTime.now())
                    AlarmService.addNotification(this,taskItem.todo_execute_remind.toString() , "execute", text_description.text.toString()+"任务要做了哦！")
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun confirm(flag: Boolean, mode: Int) {
        if(flag){
            if(mode==1) {
                taskItem.todo_deadline = addtaskViewModel.time_point.value
                if (addtaskViewModel.time_point3.value != null)
                    taskItem.todo_deadline_remind = addtaskViewModel.time_point3.value
                //提醒选择时间
                else if (addtaskViewModel.pre_time.value != null) {
                    taskItem.todo_deadline_remind = when (addtaskViewModel.pre_time.value) {
                        "当天" -> {
                            taskItem.todo_deadline
                        }
                        "提前1天" -> {
                            taskItem.todo_deadline?.minusDays(1)
                        }
                        "提前2天" -> {
                            taskItem.todo_deadline?.minusDays(2)
                        }
                        "提前3天" -> {
                            taskItem.todo_deadline?.minusDays(3)
                        }
                        "提前1周" -> {
                            taskItem.todo_deadline?.minusDays(7)
                        }
                        else -> {
                            null
                        }
                    }
                }
                if(taskItem.todo_deadline!=null) {
                    text__deadline.setText(UtiFunc.Time2String(taskItem.todo_deadline!!))
                }
                if(taskItem.todo_deadline_remind!=null){
                    imageView3.visibility= View.VISIBLE
                    textView3.text="到期提醒:"+UtiFunc.Time2String(taskItem.todo_deadline_remind!!)
                }
            }
            else if(mode==2){
                //执行开始时间
                taskItem.todo_execute_starttime= addtaskViewModel.time_point.value
                //执行结束时间
                taskItem.todo_execute_endtime= addtaskViewModel.time_point2.value
                //执行提醒时间
                //执行自定义提醒时间
                if(addtaskViewModel.time_point3.value!=null)
                    taskItem.todo_execute_remind=addtaskViewModel.time_point3.value
                //提醒选择时间
                else if(addtaskViewModel.pre_time2.value!=null){
                    taskItem.todo_execute_remind=when(addtaskViewModel.pre_time2.value){
                        "当天" -> {
                            taskItem.todo_execute_starttime
                        }
                        "提前1天" -> {
                            taskItem.todo_execute_starttime?.minusDays(1)
                        }
                        "提前2天" -> {
                            taskItem.todo_execute_starttime?.minusDays(2)
                        }
                        "提前3天" -> {
                            taskItem.todo_execute_starttime?.minusDays(3)
                        }
                        "提前1周" -> {
                            taskItem.todo_execute_starttime?.minusDays(7)
                        }
                        else -> {
                            null
                        }
                    }
                }
                if(taskItem.todo_execute_starttime!=null)
                    text_execute_time.setText(UtiFunc.Time2String(taskItem.todo_execute_starttime!!))
                if(taskItem.todo_execute_remind!=null){
                    imageView2.visibility= View.VISIBLE
                    textView4.text="执行提醒:"+UtiFunc.Time2String(taskItem.todo_execute_remind!!)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun checked(flag: Boolean, mode: Int?) {
        if(flag){
            if(mode==3){
                taskItem.todo_deadline_remind=addtaskViewModel.time_point3.value
                imageView3.visibility= View.VISIBLE
                textView3.text="到期提醒:"+UtiFunc.Time2String(taskItem.todo_deadline_remind!!)
            }
            else if(mode==4){
                taskItem.todo_execute_remind=addtaskViewModel.time_point3.value
                imageView2.visibility= View.VISIBLE
                textView4.text="执行提醒:"+UtiFunc.Time2String(taskItem.todo_execute_remind!!)
            }
        }
    }
}


