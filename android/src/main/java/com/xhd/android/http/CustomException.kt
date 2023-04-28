package com.xhd.android.http

/**
 * Create by wk on 2021/7/6
 */
/**
 * @param code 错误码
 * @param message 提示
 */
class CustomException(code: String, message: String?) : Throwable(message)
