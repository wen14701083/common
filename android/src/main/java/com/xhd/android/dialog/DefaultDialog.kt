package com.xhd.android.dialog

import android.content.Context
import android.view.Gravity
import androidx.annotation.StringRes
import com.xhd.android.R
import com.xhd.android.utils.ResourcesUtils
import com.xhd.android.utils.setOnDoubleClickListener
import kotlinx.android.synthetic.main.dialog_default.view.*

/**
 * @date created on 2022/9/23
 */
class DefaultDialog {

    class Builder(context: Context) : BaseDialog.Builder<Builder>(context) {
        init {
            setContentView(R.layout.dialog_default)
            setGravity(Gravity.TOP)

            contentView?.apply {
                btn_cancel.setOnDoubleClickListener {
                    dismiss()
                }
            }
        }

        fun setTitle(@StringRes resId: Int) = setTitle(ResourcesUtils.getString(resId))

        fun setTitle(title: String) = apply {
            contentView?.tv_title?.text = title
        }

        fun setMessage(@StringRes resId: Int) = setMessage(ResourcesUtils.getString(resId))

        fun setMessage(message: String) = apply {
            contentView?.tv_message?.text = message
        }

        fun setCancel(@StringRes resId: Int) = setCancel(ResourcesUtils.getString(resId))

        fun setCancel(cancel: String) = apply {
            contentView?.btn_cancel?.text = cancel
        }

        fun setDefine(@StringRes resId: Int) = setDefine(ResourcesUtils.getString(resId))

        fun setDefine(define: String) = apply {
            contentView?.tv_define?.text = define
        }

        fun setCancelClickListener(listener: BaseDialog.OnClickListener?) = apply {
            contentView?.btn_cancel?.setOnDoubleClickListener {
                listener?.onClick(getDialog())
            }
        }

        fun setDefineClickListener(listener: BaseDialog.OnClickListener?) = apply {
            contentView?.tv_define?.setOnDoubleClickListener {
                listener?.onClick(getDialog())
            }
        }
    }
}
