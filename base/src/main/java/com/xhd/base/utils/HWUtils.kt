package com.xhd.base.utils

import android.text.TextUtils

/**
 * Content:
 * Create by wk on 2021/10/14
 */
object HWUtils {

    fun isHarmonyOs(): Boolean {
        return try {
            val clazz = Class.forName("com.huawei.system.BuildEx")
            val osBrand = clazz.getMethod("getOsBrand").invoke(clazz)
            "HARMONY".equals(osBrand?.toString(), true)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getHarmonyVersion(): String {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            val method = clazz.getDeclaredMethod("get", String::class.java)
            val value = method.invoke(clazz, "hw_sc.build.platform.version")?.toString()
            if (TextUtils.isEmpty(value)) {
                return ""
            }
            return value!!
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
}
