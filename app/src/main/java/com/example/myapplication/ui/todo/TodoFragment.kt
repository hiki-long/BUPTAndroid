package com.example.myapplication.ui.todo

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.example.myapplication.MainActivity
import com.example.myapplication.R
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
        setHasOptionsMenu(true)
        todoViewModel =
            ViewModelProvider(this).get(TodoViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_todo, container, false)
        val textView: TextView = root.findViewById(R.id.text_todo)
        todoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val bt: FloatingActionButton = root.findViewById(R.id.add)
        bt.setOnClickListener {
            //测试弹出编辑清单的窗口代码
            /*
            *@ 模块1 弹出编辑清单
            */
            var dialog = ListDialogCreate()
            //这里的bundle是用来传输标题的数据,小型的数据都可以用bundle传
            var args = Bundle()
            args.putString("title", "编辑清单")
            dialog.arguments = args
            dialog.show(parentFragmentManager, "listdialog")


            /*
            * @ 模块2 todo日期选择编辑
            * */
//            val dialog = DaySelectDialogCreate()
//            val args = Bundle()
//            args.putString("title","设置截止时间")
//            args.putInt("mode",1)
//            dialog.arguments = args
//            dialog.show(parentFragmentManager, "timeselect")
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).needDrawer(true)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_ordinarylist,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editlist -> {

            }
            R.id.deletelist -> {

            }
            R.id.sort -> {
                val myItems = listOf("清单", "创建日期", "截至日期", "执行事件", "重要级")
                MaterialDialog(requireContext()).show {
                    cornerRadius(16f)
                    listItemsSingleChoice(items = myItems) { dialog, index, text ->
                        when (index) {
                            0 -> {
                                Toast.makeText(this.context, "you clicked 0", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    positiveButton(R.string.confirm)
                }
            }
            R.id.showCompleted -> {

            }
        }
        return true;
    }
}