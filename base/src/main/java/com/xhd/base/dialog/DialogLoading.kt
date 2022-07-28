package com.xhd.base.dialog

import android.content.Context
import android.view.Gravity
import androidx.annotation.LayoutRes
import com.xhd.base.R
import com.xhd.base.utils.ResourcesUtils

/**
 * Create by wk on 2021/7/6
 */
class DialogLoading : BaseDialogFragment {
    constructor(): super()
    constructor(context: Context, params: DialogFragmentParams?): super(context, params)

    class Builder(context: Context, @LayoutRes layoutRes: Int = R.layout.dialog_loading) :
        BaseDialogFragment.Builder<Builder, DialogLoading>(context, layoutRes) {

        init {
            setWidth(ResourcesUtils.getDimens(R.dimen.dp_80))
            setHeight(ResourcesUtils.getDimens(R.dimen.dp_80))
            setGravity(Gravity.CENTER)
            setBackgroundTransparent(true)
            setCancelableOutside(false)
        }

        override fun build(): DialogLoading {
            return DialogLoading(context, buildParams)
        }
    }
}