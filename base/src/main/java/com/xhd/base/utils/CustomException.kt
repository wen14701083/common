package com.xhd.base.utils

/**
 * Create by wk on 2021/7/6
 */
/**
 * @param code 错误码
 * @param message 提示
 * @param isList 是否列表错误
 */
class CustomException(val code: String, message: String?, val isList: Boolean = false) : Exception(message)
