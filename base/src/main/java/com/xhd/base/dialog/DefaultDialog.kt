package com.xhd.base.dialog

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.xhd.base.R
import com.xhd.base.utils.setOnDoubleClickListener

/**
 * Create by wk on 2021/7/2
 */
open class DefaultDialog : BaseDialogFragment {

    constructor(): super()
    constructor(context: Context, params: DialogFragmentParams?): super(context, params)

    var tvTitle : TextView ?= null
    var tvMessage : TextView ?= null
    var tvDefine : TextView ?= null
    var btnCancel : View ?= null

    override fun initView(view: View) {
        super.initView(view)
        if (mRootView == null || mParams == null) {
            return
        }
        tvTitle = mRootView!!.findViewById(R.id.tv_title)
        tvMessage = mRootView!!.findViewById(R.id.tv_message)
        tvDefine = mRootView!!.findViewById(R.id.tv_define)
        btnCancel = mRootView!!.findViewById(R.id.btn_cancel)

        if (!TextUtils.isEmpty(mParams?.title)) {
            tvTitle?.text = mParams?.title
        }
        if (!TextUtils.isEmpty(mParams?.message)) {
            tvMessage?.text = mParams?.message
        }
        if (!TextUtils.isEmpty(mParams?.defineText)) {
            tvDefine?.text = mParams?.defineText
        }
        if (btnCancel is TextView && !TextUtils.isEmpty(mParams?.cancelText)) {
            (btnCancel as TextView).text = mParams?.cancelText
        }

        tvDefine?.setOnDoubleClickListener {
            mParams?.defineClickListener?.onClick(this)
        }
        btnCancel?.setOnDoubleClickListener {
            if (mParams?.cancelClickListener == null) {
                dismiss()
            } else {
                mParams?.cancelClickListener?.onClick(this)
            }
        }
    }

    open class Builder(context: Context, @LayoutRes layoutRes: Int) :
        BaseDialogFragment.Builder<Builder, DefaultDialog>(context, layoutRes) {

        fun setNormalPadding() : Builder {
            setStartPadding(30)
            setEndPadding(30)
            setTopPadding(100)
            setBottomPadding(100)
            return this
        }

        override fun build(): DefaultDialog {
            return DefaultDialog(context, buildParams)
        }
    }
}