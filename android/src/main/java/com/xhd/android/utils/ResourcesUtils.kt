package com.xhd.android.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.xhd.android.base.BaseApplication

/**
 * Create by wk on 2021/6/29
 */
object ResourcesUtils {

    private fun getResources(): Resources {
        return BaseApplication.context.resources
    }

    fun getString(@StringRes resId: Int): String {
        return getResources().getString(resId)
    }

    fun getDimens(@DimenRes resId: Int): Int {
        return getResources().getDimensionPixelSize(resId)
    }

    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(BaseApplication.context, resId)
    }

    fun dp2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    fun isPad(context: Context): Boolean {
        return (getResources().configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    fun getScreenHeight(activity: Activity): Int {
        return getResources().displayMetrics.heightPixels
    }

    fun getScreenWidth(activity: Activity): Int {
        return getResources().displayMetrics.widthPixels
    }

    fun copy(data: String): Boolean {
        return try {
            val service = BaseApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            service.setPrimaryClip(ClipData.newPlainText(null, data))
            true
        } catch (e: Exception) {
            false
        }
    }
}
