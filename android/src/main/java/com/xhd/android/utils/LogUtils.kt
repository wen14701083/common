package com.xhd.android.utils

import android.util.Log
import com.xhd.android.BuildConfig

/**
 * Content:
 * Create by wk on 2021/7/1
 */
object LogUtils {

    private const val TAG = "+DEBUG+"

    private fun generateTag(): String {
        Thread.currentThread().stackTrace.find {
            !(it.isNativeMethod || it.className == Thread::class.java.name || it.className == LogUtils::class.java.name)
        }?.let {
            val className = it.className
            val lastName = className.substring(className.lastIndexOf(".") + 1)
            return "$TAG[$lastName.${it.methodName}:(L:${it.lineNumber})]"
        }
        return TAG
    }

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(generateTag(), msg)
        }
    }

    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(generateTag(), msg)
        }
    }

    fun w(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.w(generateTag(), msg)
        }
    }

    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(generateTag(), msg)
        }
    }
}

fun String.logE() {
    LogUtils.e(this)
}

fun String.logD() {
    LogUtils.d(this)
}
