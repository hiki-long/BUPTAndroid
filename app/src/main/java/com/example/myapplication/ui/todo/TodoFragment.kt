package com.example.myapplication.ui.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.model.TaskPriority
import com.example.myapplication.model.TaskState
import com.example.myapplication.utils.SoftInputUtil
import com.example.myapplication.utils.SoftInputUtil.ISoftInputChanged
import com.example.myapplication.utils.SoftKeyBoardListener
import com.example.myapplication.utils.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import com.example.myapplication.db.entity.TaskEntity
import com.example.myapplication.ui.adapter.TaskAdapter
import com.example.myapplication.ui.fragment.AddTaskActivity
import com.example.myapplication.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.OffsetDateTime


@AndroidEntryPoint
class TodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var tasklist:ArrayList<TaskEntity>
    private var adapter= TaskAdapter()
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
//        val textView: TextView = root.findViewById(R.id.text_todo)
//        todoViewModel.text.observe(viewLifecycleOwner, Observer {
//            //textView.text = it
//        })

        mainViewModel.getTasks().observe(
                viewLifecycleOwner,
                {
                    tasklist = it as ArrayList<TaskEntity>
                    adapter.submitList(tasklist)
                    findNavController().navigateUp()
                }
        )

        val bt: FloatingActionButton = root.findViewById(R.id.add)
        bt.setOnClickListener {
            mainViewModel.insertTask(OffsetDateTime.now(),TaskState.DOING,"测试实例1",1,TaskPriority.COMMON, OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(), OffsetDateTime.now(),"hhhhhh").observe(
                    viewLifecycleOwner,
                    {
                        findNavController().navigateUp()
                    }
            )
            var intent= Intent(requireActivity(), AddTaskActivity::class.java)
            startActivity(intent)
        }
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).needDrawer(true)

        postponeEnterTransition()
        recyclerview.layoutManager= LinearLayoutManager(requireActivity())
        recyclerview.adapter=adapter
        recyclerview.doOnPreDraw {
            startPostponedEnterTransition()
        }
//        var labels = ArrayList<String>()
//        labels.add("花哪儿记账")
//        labels.add("给未来写封信")
//        labels.add("我也不知道我是谁")
//        stackLabelView.setDeleteButton(true)
//        stackLabelView.labels = labels
//        stackLabelView.onLabelClickListener = OnLabelClickListener { index, v, s ->
//            if (stackLabelView.isDeleteButton) {      //是否开启了删除模式
//                //删除并重新设置标签
//                labels.removeAt(index)
//                stackLabelView.labels = labels
//            } else {
//                Toast.makeText(requireContext(), "点击了：$s", Toast.LENGTH_SHORT).show()
//            }
//        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END){
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
//                var wordFrom=allWords.get(viewHolder.adapterPosition)
//                var wordTo=allWords.get(target.adapterPosition)
//                var idtemp=wordFrom.id
//                wordFrom.id=wordTo.id
//                wordTo.id=idtemp
//                wordViewModel.updateWords(wordFrom,wordTo)
                adapter.notifyItemMoved(viewHolder.adapterPosition,target.adapterPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                var word=allWords.get(viewHolder.adapterPosition)
//                wordViewModel.deleteWords(word)
                var p = tasklist.removeAt(viewHolder.adapterPosition)
                var flag=true
                Snackbar.make(requireActivity().findViewById(R.id.fragment_todo),"删除一项task", Snackbar.LENGTH_SHORT)
                        .setAction("撤销"){
                            tasklist.add(p)
                            flag=false
                            adapter.notifyDataSetChanged()
                        }.show()
                GlobalScope.launch {
                    Thread.sleep(5000)
                    if(flag)
                        mainViewModel.deleteTask(p.todo_id)
                }
                adapter.notifyDataSetChanged()
            }
        }).attachToRecyclerView(recyclerview)


        //测试detail页面代码：
//        testButton.setOnClickListener {
//            TodoItemDetailActivity.actionStart(context)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_ordinarylist, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editlist -> {
                var dialog = ListDialogCreate()
                var args = Bundle()
                args.putString("title", "编辑清单")
                dialog.arguments = args
                dialog.show(parentFragmentManager, "listdialog")
            }
            R.id.deletelist -> {

            }
            R.id.sort -> {
                val myItems = listOf("清单", "创建日期", "截至日期", "执行事件", "重要级")
                MaterialDialog(requireContext()).show {
                    cornerRadius(16f)
                    title(R.string.sort)
                    listItemsSingleChoice(items = myItems,waitForPositiveButton = false){ dialog, index, text ->
                        when(index){
                            0->{
                                Toast.makeText(this.context,"you clicked 0",Toast.LENGTH_SHORT).show()
                                dismiss()
                            }
                            1->{
                                dismiss()
                            }
                            2->{
                                dismiss()
                            }
                            3->{
                                dismiss()
                            }
                            4->{
                                dismiss()
                            }
                        }
                    }
                    negativeButton(R.string.cancel)
                }
            }
            R.id.showCompleted -> {
            }
        }
        return true;
    }
}