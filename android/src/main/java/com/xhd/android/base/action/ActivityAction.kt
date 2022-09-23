package com.xhd.android.base.action

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

interface ActivityAction {

    fun getContext(): Context

    fun getActivity(): Activity? {
        var context: Context? = getContext()
        do {
            when (context) {
                is Activity -> return context
                is ContextWrapper -> context = context.baseContext
                else -> return null
            }
        } while (context != null)
        return null
    }
}
