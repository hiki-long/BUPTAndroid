package com.example.myapplication.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.myapplication.R
import kotlinx.android.synthetic.main.self_design_collectbox_slide_item_view.view.*


class TodoItemView(context: Context, attrs: AttributeSet): LinearLayout(context,attrs){
    init{
        val attributes:TypedArray=context.obtainStyledAttributes(attrs, R.styleable.self_design_collect_box_item_view)
        val description:String=attributes.getString(R.styleable.self_design_collect_box_item_view_description)?:""
        val image=attributes.getDrawable(R.styleable.self_design_collect_box_item_view_android_src)
        val num=attributes.getString(R.styleable.self_design_collect_box_item_view_num)
        attributes.recycle()
        LayoutInflater.from(context).inflate(R.layout.self_design_collectbox_slide_item_view,this)
        self_design_item_imageView.setImageDrawable(image)
        self_design_item_textView.text=description
        self_design_item_num.text=num
    }

    fun updateDescription(description:String){
        self_design_item_textView.text=description
    }

    fun undateNum(num:String){
        self_design_item_num.text=num
    }

    fun updateNum(num:Int){
        self_design_item_num.text=num.toString()
    }

    fun updateIcon(drawable: Drawable){
        self_design_item_imageView.setImageDrawable(drawable)
    }
}