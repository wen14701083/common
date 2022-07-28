package com.xhd.base.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.xhd.base.base.BaseApplication

/**
 * Create by wk on 2021/7/8
 */
object SpUtils {

    private fun getSharedPreferences(): SharedPreferences {
        return BaseApplication.context.getSharedPreferences("", Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String?) {
        getSharedPreferences().edit {
            putString(key, value)
        }
    }

    fun getString(key: String): String {
        return getSharedPreferences().getString(key, "").toString()
    }

    fun getBoolean(key: String): Boolean {
        return getSharedPreferences().getBoolean(key, false)
    }

    fun putBoolean(key: String, value: Boolean) {
        getSharedPreferences().edit {
            putBoolean(key, value)
        }
    }

    fun delete(key: String) {
        getSharedPreferences().edit {
            remove(key)
        }
    }
}
