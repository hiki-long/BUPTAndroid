package com.example.myapplication.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

class SoftInputUtil {
    private var softInputHeight = 0
    private var softInputHeightChanged = false
    private var anyView: View? = null
    private var listener: ISoftInputChanged? = null
    private var isSoftInputShowing = false

    interface ISoftInputChanged {
        fun onChanged(isSoftInputShow: Boolean, softInputHeight: Int, viewOffset: Int)
    }

    fun attachSoftInput(anyView: View?, listener: ISoftInputChanged?) {
        if (anyView == null || listener == null) return

        //根View
        val rootView = anyView.rootView ?: return

        this.anyView = anyView
        this.listener = listener

        rootView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            //对于Activity来说，该高度即为屏幕高度
            val rootHeight = rootView.height
            val rect = Rect()
            //获取当前可见部分，默认可见部分是除了状态栏和导航栏剩下的部分
            rootView.getWindowVisibleDisplayFrame(rect)

            var isSoftInputShow = false
            var softInputHeight = 0

            if (rootHeight> rect.bottom) {
                //除去导航栏高度后，可见区域仍然小于屏幕高度，则说明键盘弹起了
                isSoftInputShow = true
                //键盘高度
                softInputHeight = rootHeight - rect.bottom
                if (this.softInputHeight !== softInputHeight) {
                    softInputHeightChanged = true
                    this.softInputHeight = softInputHeight
                } else {
                    softInputHeightChanged = false
                }
            }

            //获取目标View的位置坐标
            val location = IntArray(2)
            anyView.getLocationOnScreen(location)

            //条件1减少不必要的回调，只关心前后发生变化的
            //条件2针对软键盘切换手写、拼音键等键盘高度发生变化
            if (isSoftInputShowing != isSoftInputShow || isSoftInputShow && softInputHeightChanged) {
                if (listener != null) {
                    //第三个参数为该View需要调整的偏移量
                    //此处的坐标都是相对屏幕左上角(0,0)为基准的
                    listener.onChanged(isSoftInputShow, softInputHeight, location[1] + anyView.height - rect.bottom)

                }
                isSoftInputShowing = isSoftInputShow
            }
        }
    }

    companion object {

        fun showSoftInput(view: View?) {
            if (view == null) return
            view.requestFocus()
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.showSoftInput(view, 0)
        }

        fun hideSoftInput(view: View?) {
            if (view == null) return
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}