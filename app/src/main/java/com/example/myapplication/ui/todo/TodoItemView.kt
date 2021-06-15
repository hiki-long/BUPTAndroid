package com.example.myapplication.ui.todo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.todo_item_list_item_view.view.*

//To-do 界面一个item的自定义view
class TodoItemView(context: Context, attrs: AttributeSet): LinearLayout(context,attrs){
    init{
        val attributes:TypedArray=context.obtainStyledAttributes(attrs, R.styleable.self_design_todo_item_view)
        val description:String=attributes.getString(R.styleable.self_design_todo_item_view_description)?:""
        val image=attributes.getDrawable(R.styleable.self_design_todo_item_view_android_src)
        val num=attributes.getString(R.styleable.self_design_todo_item_view_num)
        attributes.recycle()
        LayoutInflater.from(context).inflate(R.layout.todo_item_list_item_view,this)
        todo_item_list_item_view_imageView.setImageDrawable(image)
        todo_item_list_item_view_name.text=description
        todo_item_list_item_view_num.text=num
    }

    fun updateItemName(description:String){
        todo_item_list_item_view_name.text=description
    }

    fun updateTodoNum(num:String){
        todo_item_list_item_view_num.text=num
    }

    fun updateTodoNum(num:Int){
        todo_item_list_item_view_num.text=num.toString()
    }

    fun updateIcon(drawable: Drawable){
        todo_item_list_item_view_imageView.setImageDrawable(drawable)
    }
}