package com.xhd.android.ktx

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.xhd.android.base.BaseActivity

/**
 * @date created on 2022/9/21
 */
fun startActivity(context: Context, clazz: Class<BaseActivity<*>>, block: Intent.() -> Unit = {}) {
    val intent = Intent(context, clazz)
    intent.apply(block)
    if (context !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
