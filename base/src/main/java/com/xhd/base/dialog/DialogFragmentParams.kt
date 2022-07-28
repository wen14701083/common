package com.xhd.base.dialog

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

/**
 * Content: dialog参数
 * Create by wk on 2021/7/2
 */
class DialogFragmentParams(@LayoutRes var layoutRes: Int = -1) {
    var height = ViewGroup.LayoutParams.WRAP_CONTENT
    var width = ViewGroup.LayoutParams.MATCH_PARENT
    var gravity = Gravity.CENTER
    var isCancelableOutside = true
    var isCancelable = true
    var backgroundTransparent = false
    var topPadding = 0
    var bottomPadding = 0
    var startPadding = 0
    var endPadding = 0
    var title : String ?= ""
    var message : String ?= ""
    var imageUrl : String ?= ""
    var defineText : String ?= "确定"
    var cancelText : String ?= "取消"
    @DrawableRes var imageRes : Int ?= -1
    // 设置图形圆角
    var round = 0
    // 自定义view
    var view: View? = null

    var cancelClickListener : BaseDialogFragment.OnClickListener ?= null
    var defineClickListener : BaseDialogFragment.OnClickListener ?= null
    var imageClickListener : BaseDialogFragment.OnClickListener ?= null
}