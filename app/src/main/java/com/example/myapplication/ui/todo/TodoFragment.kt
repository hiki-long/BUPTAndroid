package com.example.myapplication.ui.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.utils.SoftInputUtil
import com.example.myapplication.utils.SoftInputUtil.ISoftInputChanged
import com.example.myapplication.utils.SoftKeyBoardListener
import com.example.myapplication.utils.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kongzue.stacklabelview.interfaces.OnLabelClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo.*


@AndroidEntryPoint
class TodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel

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
        val textView: TextView = root.findViewById(R.id.text_todo)
        todoViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val remind_time = root.findViewById<ImageView>(R.id.remind_time)
        //这里是popumMenu使用的例子，仅供参考
        //1、初始化popupMenu
        val popupMenu1 = PopupMenu(
            root.context,
            remind_time
        )
        //2、添加menu项目
        popupMenu1.menu.add(Menu.NONE, 0, 0, "截止时间提醒")
        popupMenu1.menu.add(Menu.NONE, 1, 1, "执行时间提醒")
        //3、添加响应事件
        popupMenu1.setOnMenuItemClickListener(fun(it: MenuItem): Boolean {
            when(it.itemId) {
                0 -> {
                    val temp = Bundle()
                    temp.putInt("mode", 3)
                    val timedialog = TimeBarDialogCreate()
                    timedialog.arguments = temp
                    timedialog.show(parentFragmentManager, "ShowTimeBar")
                }
                1 -> {
                    val temp = Bundle()
                    temp.putInt("mode", 3)
                    val timedialog = TimeBarDialogCreate()
                    timedialog.arguments = temp
                    timedialog.show(parentFragmentManager, "ShowTimeBar")
                }
            }

            return true
        })
        remind_time?.setOnClickListener {
            popupMenu1.show()
        }
        val end_time_button = root.findViewById<ImageView>(R.id.end_time)
        end_time_button.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title","设置截止时间")
            args.putInt("mode",1)
            dialog.arguments = args
            dialog.show(parentFragmentManager, "timeselect")
        }
        val execute_time_button = root.findViewById<ImageView>(R.id.execute_time)
        execute_time_button.setOnClickListener {
            val dialog = DaySelectDialogCreate()
            val args = Bundle()
            args.putString("title","设置执行时间")
            args.putInt("mode",2)
            dialog.arguments = args
            dialog.show(parentFragmentManager, "timeselect")
        }

        //下面应该是根据数据库表来动态生成menu
        val project_type = root.findViewById<ImageView>(R.id.project_type)
        val popupMenu2 = PopupMenu(
                root.context,
                project_type
        )
        popupMenu2.menu.add(Menu.NONE, 0, 0, "收件箱")
        popupMenu2.menu.add(Menu.NONE, 1, 1, "更多清单")
        popupMenu2.setOnMenuItemClickListener (fun(it: MenuItem): Boolean{
            when(it.itemId){
                //这里之后填充点击的事件

            }
            return true
        })
        project_type.setOnClickListener {
            popupMenu2.show()
        }

        val bt: FloatingActionButton = root.findViewById(R.id.add)
        bt.setOnClickListener {
            //测试弹出编辑清单的窗口代码
            /*
            *@ 模块1 弹出编辑清单
            var dialog = ListDialogCreate()
            //这里的bundle是用来传输标题的数据,小型的数据都可以用bundle传
            var args = Bundle()
            args.putString("title", "编辑清单")
            dialog.arguments = args
            dialog.show(parentFragmentManager, "listdialog")
            */
            var editTexten=requireActivity().findViewById<EditText>(R.id.editText_todo)
            var cardView=requireActivity().findViewById<CardView>(R.id.card_todo)
            SoftInputUtil.showSoftInput(editTexten)
            cardView.visibility=View.VISIBLE
            val softInputUtil = SoftInputUtil()
            softInputUtil.attachSoftInput(cardView, object : ISoftInputChanged {
                override fun onChanged(isSoftInputShow: Boolean, softInputHeight: Int, viewOffset: Int) {
                    if (isSoftInputShow) {
                        cardView.translationY = cardView.translationY - viewOffset
                    } else {
                        //cardView.visibility = View.GONE
                    }
                }
            })

            /**
             * 添加软键盘监听
             */
            var softKeyBoardListener = SoftKeyBoardListener(requireActivity())
            //软键盘状态监听
            softKeyBoardListener.setListener(object : OnSoftKeyBoardChangeListener {
                override fun keyBoardShow(height: Int) {
                    //软键盘已经显示，做逻辑
                    editTexten.setText("dddddddddddddddddddddddddddddd".toCharArray(),0,30)
                    editTexten.setText("".toCharArray(),0,0)
                    cardView.visibility=View.VISIBLE
                }

                override fun keyBoardHide(height: Int) {
                    //软键盘已经隐藏,做逻辑
                    cardView.visibility = View.GONE
                }
            })


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
        var labels = ArrayList<String>()
        labels.add("花哪儿记账")
        labels.add("给未来写封信")
        labels.add("我也不知道我是谁")
        stackLabelView.setDeleteButton(true)
        stackLabelView.labels = labels
        stackLabelView.onLabelClickListener = OnLabelClickListener { index, v, s ->
            if (stackLabelView.isDeleteButton) {      //是否开启了删除模式
                //删除并重新设置标签
                labels.removeAt(index)
                stackLabelView.labels = labels
            } else {
                Toast.makeText(requireContext(), "点击了：$s", Toast.LENGTH_SHORT).show()
            }
        }

        //测试detail页面代码：
        testButton.setOnClickListener {
            TodoItemDetailActivity.actionStart(context)
        }
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