package com.xhd.base.utils

import android.os.SystemClock
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.youth.banner.listener.OnBannerListener
import java.util.*
import kotlin.concurrent.schedule

/**
 * Content: 防止快速点击
 * Create by wk on 2021/6/29
 */
object DoubleClickHelper {

    private const val INTERVAL_TIME = 500L
    private val TIME_ARRAY = LongArray(2)

    fun isDoubleClick(): Boolean {
        System.arraycopy(TIME_ARRAY, 1, TIME_ARRAY, 0, TIME_ARRAY.size - 1)
        TIME_ARRAY[TIME_ARRAY.size - 1] = SystemClock.uptimeMillis()
        return TIME_ARRAY[0] >= (SystemClock.uptimeMillis() - INTERVAL_TIME)
    }
}

fun singClick(block: () -> Unit) {
    if (DoubleClickHelper.isDoubleClick()) {
        return
    } else {
        block()
    }
}

/**
 * view点击事件
 */
abstract class OnDoubleClickListener : View.OnClickListener {

    companion object {

        var enabled = true
    }

    override fun onClick(view: View) {
        if (enabled) {
            enabled = false
            view.postDelayed({ enabled = true }, 500)
            doClick(view)
        }
    }

    abstract fun doClick(view: View)
}

/**
 * banner点击事件
 */
abstract class OnDoubleClickBannerListener<T> : OnBannerListener<T> {

    companion object {

        var enabled = true
    }

    override fun OnBannerClick(data: T, position: Int) {
        if (enabled) {
            enabled = false
            Timer().schedule(500L) {
                enabled = true
            }
            doClick(data, position)
        }
    }

    abstract fun doClick(data: T, position: Int)
}

/**
 * recycler点击事件
 */
abstract class OnDoubleItemClickListener<T> : OnItemClickListener {

    companion object {

        var enabled = true
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (enabled) {
            enabled = false
            Timer().schedule(500L) {
                enabled = true
            }
            val item = adapter.getItem(position)
            if (item != null) {
                doClick(adapter, item as T, position)
            }
        }
    }

    abstract fun doClick(adapter: BaseQuickAdapter<*, *>, item: T, position: Int)
}

fun View.setOnDoubleClickListener(block: () -> Unit) {
    this.setOnClickListener(object : OnDoubleClickListener() {
        override fun doClick(view: View) {
            block()
        }
    })
}

fun <T> BaseQuickAdapter<T, *>.setOnItemDoubleClickListener(block: (adapter: BaseQuickAdapter<*, *>, item: T, position: Int) -> Unit) {
    this.setOnItemClickListener(object : OnDoubleItemClickListener<T>() {
        override fun doClick(adapter: BaseQuickAdapter<*, *>, item: T, position: Int) {
            block(adapter, item, position)
        }
    })
}
