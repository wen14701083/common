package com.xhd.android.utils

import android.app.Activity
import java.util.*

/**
 * Create by wk on 2021/6/29
 */
object ActivityManager {

    private val mActivityStack by lazy { Stack<Activity>() }

    fun addActivity(activity: Activity) {
        mActivityStack.push(activity)
    }

    fun removeActivity(activity: Activity) {
        activity.finish()
        mActivityStack.remove(activity)
    }

    fun removeActivity(clazz: Class<Activity>) {
        if (hasActivity(clazz)) {
            mActivityStack.forEach {
                if (clazz.name == it::class.java.name) {
                    removeActivity(it)
                    return
                }
            }
        }
    }

    fun removeAllActivity() {
        mActivityStack.forEach {
            it.finish()
        }
        mActivityStack.clear()
    }

    fun <T : Activity> finishActivity(clazz: Class<T>) {
        mActivityStack.forEach {
            if (clazz.name == it::class.java.name) {
                it.finish()
                return
            }
        }
    }

    private fun hasActivity(clazz: Class<Activity>): Boolean {
        mActivityStack.forEach {
            if (clazz.name == it::class.java.name) {
                return !it.isDestroyed && !it.isFinishing
            }
        }
        return false
    }

    fun getCurrentActivity(): Activity {
        return mActivityStack.lastElement()
    }

    fun getActivityCount(): Int {
        return mActivityStack.size
    }
}
