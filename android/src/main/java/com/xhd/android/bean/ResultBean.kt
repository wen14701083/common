package com.xhd.android.bean

/**
 * Create by wk on 2021/6/30
 */
open class HttpResult(open val code: String, open val msg: String)

data class ResultBean<T>(val data: T, override val code: String, override val msg: String) :
    HttpResult(code, msg)

data class ResultListBean<T>(
    val data: MutableList<T>,
    override val code: String,
    override val msg: String
) : HttpResult(code, msg)
