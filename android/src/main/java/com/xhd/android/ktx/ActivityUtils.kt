package com.xhd.android.ktx

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 * @date created on 2022/9/21
 */
fun startActivity(context: Context, clazz: Class<out Activity>, block: Intent.() -> Unit = {}) {
    val intent = Intent(context, clazz)
    intent.apply(block)
    if (context !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}
