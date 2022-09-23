package com.xhd.android.dialog

import android.content.Context
import android.view.Gravity
import com.xhd.android.R
import com.xhd.android.utils.ResourcesUtils

/**
 * Create by wk on 2021/7/6
 */
class DialogLoading {

    class Builder(context: Context) : BaseDialog.Builder<Builder>(context) {

        init {
            setContentView(R.layout.dialog_loading)
            setWidth(ResourcesUtils.getDimens(R.dimen.dp_80))
            setHeight(ResourcesUtils.getDimens(R.dimen.dp_80))
            setGravity(Gravity.CENTER)
            setCanceledOnTouchOutside(false)
        }
    }
}
