package com.xhd.base.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonParser

/**
 * Create by wk on 2021/7/8
 */
object GsonUtils {

    private val gson = Gson()
    fun gsonString(any: Any): String {
        return gson.toJson(any)
    }

    fun <T> gson2Bean(json: String, clazz: Class<T>): T {
        return gson.fromJson<T>(json, clazz)
    }

    fun <T> gson2List(json: String, clazz: Class<T>): MutableList<T>? {
        if (TextUtils.isEmpty(json)) {
            return null
        }
        return try {
            val list = ArrayList<T>()
            val jsonArray = JsonParser().parse(json).asJsonArray
            jsonArray.forEach {
                list.add(gson.fromJson(it, clazz))
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
