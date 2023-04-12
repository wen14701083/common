package com.xhd.android.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity

/**
 * Create by wk on 2021/7/22
 */
object KeyboardUtils {
    fun showKeyboard(context: Context, editText: EditText) {
        val service = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
        service.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun hintKeyboard(context: Context, editText: EditText) {
        val service = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        service.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /**
     * 隐藏键盘.
     */
    fun hideSoftInput(activity: FragmentActivity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 显示键盘
     *
     */
    fun showSoftInput(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }
}
