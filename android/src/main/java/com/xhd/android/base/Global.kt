package com.xhd.android.base

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.xhd.android.R

object Global {

    var mEmptyDesc = "暂无数据"
    var mEmptyIcon = R.drawable.icon_empty_default
    var mEmptyView = R.layout.empty_list

    /**
     * layout 默认布局资源
     * desc 默认提示-文本id需设置tv_empty
     * icon 默认图片-图片id需设置iv_empty
     */
    fun initEmpty(@LayoutRes layout: Int, desc: String, @DrawableRes icon: Int) {
        mEmptyView = layout
        mEmptyIcon = icon
        mEmptyDesc = desc
    }
}
