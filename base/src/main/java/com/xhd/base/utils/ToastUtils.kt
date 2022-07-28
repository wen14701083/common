package com.xhd.base.utils

import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import com.xhd.base.base.BaseApplication

/**
 * Create by wk on 2021/7/1
 */
object ToastUtils {

    private val mToast by lazy { Toast.makeText(BaseApplication.context, "", Toast.LENGTH_SHORT) }

    fun toast(@StringRes resId: Int) {
        showToast(ResourcesUtils.getString(resId))
    }

    private fun showToast(msg: String) {
        kotlin.runCatching {
            mToast?.apply {
                setText(msg)
                duration = Toast.LENGTH_SHORT
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    @JvmStatic
    fun toast(message: String) {
        ktxRunOnUi {
            showToast(message)
        }
    }
}

fun Any.toast(string: String) {
    ToastUtils.toast(string)
}

fun Any.toast(@StringRes resId: Int) {
    ToastUtils.toast(resId)
}
