package com.xhd.android.bean

/**
 * Create by wk on 2021/6/30
 */
data class ResultBean<T>(val data: T, val code: String, val msg: String)

data class ResultListBean<T>(val data: MutableList<T>, val code: String, val msg: String)
