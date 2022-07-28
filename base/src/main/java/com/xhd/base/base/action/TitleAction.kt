package com.xhd.base.base.action

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.forEach
import com.hjq.bar.OnTitleBarListener
import com.hjq.bar.TitleBar
import com.xhd.base.utils.singClick

/**
 * 统一标题栏
 * @date created on 2022/7/14
 */
interface TitleAction : OnTitleBarListener {

    /**
     * 获取标题栏
     */
    fun getTitleBar(): TitleBar?

    /**
     * 建议不需要使用的重写，返回false，避免递归查询title
     * 默认存在title
     */
    fun hasTitle() = true

    /**
     * 左边被点击
     */
    override fun onLeftClick(titleBar: TitleBar?) {}

    override fun onTitleClick(titleBar: TitleBar?) {}

    /**
     * 右边被点击
     */
    override fun onRightClick(titleBar: TitleBar?) {}

    /**
     * 设置标题
     */
    fun setTitleBar(@StringRes id: Int) {
        getTitleBar()?.setTitle(id)
    }

    fun setTitleBar(title: String?) {
        getTitleBar()?.title = title
    }

    /**
     * 设置左边
     */
    fun setLeftTitle(id: Int) = getTitleBar()?.setLeftTitle(id)

    fun setLeftTitle(text: String?) = getTitleBar()?.setLeftTitle(text)

    fun setLeftIcon(id: Int) = getTitleBar()?.setLeftIcon(id)

    /**
     * 设置右边
     */
    fun setRightTitle(id: Int) = getTitleBar()?.setRightTitle(id)

    fun setRightTitle(text: String?) = getTitleBar()?.setRightTitle(text)

    fun setRightIcon(id: Int) = getTitleBar()?.setRightIcon(id)

    /**
     * 递归自动获取TitleBar
     */
    fun obtainTitleBar(group: ViewGroup?): TitleBar? {
        if (!hasTitle()) {
            return null
        }
        group?.forEach {
            when (it) {
                is TitleBar -> return it

                is ViewGroup -> {
                    val titleBar = obtainTitleBar(it)
                    if (titleBar != null) {
                        return titleBar
                    }
                }
            }
        }
        return null
    }
}
