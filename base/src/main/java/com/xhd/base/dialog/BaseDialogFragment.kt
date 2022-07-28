package com.xhd.base.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.xhd.base.R
import com.xhd.base.utils.ResourcesUtils

/**
 * Create by wk on 2021/6/29
 */

open class BaseDialogFragment : BaseDialog {
    var mParams : DialogFragmentParams? = null

    constructor() : super()
    constructor(mContext: Context, params: DialogFragmentParams?) : super(mContext) {
        this.mParams = params
    }

    override fun onStart() {
        super.onStart()
        if (mWindow == null || mParams == null) {
            return
        }
        mWindow?.setLayout(mParams!!.width, mParams!!.height)
        if (mParams!!.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            mWindow?.decorView?.setPadding(
                mParams!!.startPadding,
                mParams!!.topPadding,
                mParams!!.endPadding,
                mParams!!.bottomPadding
            )
        }
        if (mParams!!.backgroundTransparent) {
            setBackgroundTransparent(mWindow)
        }
    }

    override fun initGravity(): Int {
        return mParams?.gravity ?: Gravity.CENTER
    }

    override fun initView(view: View) {

    }

    protected open fun setBackgroundTransparent(window: Window?) {
        val lp = window?.attributes
        lp?.dimAmount = 0f
        window?.attributes = lp
    }

    override fun initDialog() {
        dialog?.setCanceledOnTouchOutside(mParams?.isCancelableOutside ?: true)
        isCancelable = mParams?.isCancelable ?: false
    }

    override fun attachLayoutRes() : Int {
        return mParams?.layoutRes ?: R.layout.dialog_default
    }

    open class Builder<B : Builder<B, D>, D : BaseDialogFragment>(var context: Context, @LayoutRes layoutRes: Int) {
        protected var buildParams: DialogFragmentParams = DialogFragmentParams(layoutRes)

        fun normalPadding() {
            setStartPadding(32)
            setEndPadding(32)
            setTopPadding(100)
            setBottomPadding(100)
        }

        fun setCancelable(isCancelable: Boolean): B {
            buildParams.isCancelable = isCancelable
            return this as B
        }

        fun setCancelableOutside(isCancelableOutside: Boolean): B {
            buildParams.isCancelableOutside  = isCancelableOutside
            return this as B
        }

        fun setHeight(height: Int): B {
            buildParams.height = height
            return this as B
        }

        fun setWidth(width: Int): B {
            buildParams.width = width
            return this as B
        }

        fun setBackgroundTransparent(backgroundTransparent: Boolean): B {
            buildParams.backgroundTransparent = backgroundTransparent
            return this as B
        }

        fun setGravity(gravity: Int): B {
            buildParams.gravity = gravity
            return this as B
        }

        fun setStartPadding(padding: Int): B {
            if (buildParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            }
            buildParams.startPadding = ResourcesUtils.dp2px(padding.toFloat())
            return this as B
        }

        fun setEndPadding(padding: Int): B {
            if (buildParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            }
            buildParams.endPadding = ResourcesUtils.dp2px(padding.toFloat())
            return this as B
        }

        fun setTopPadding(padding: Int): B {
            if (buildParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            }
            buildParams.topPadding = ResourcesUtils.dp2px(padding.toFloat())
            return this as B
        }

        fun setBottomPadding(padding: Int): B {
            if (buildParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            }
            buildParams.bottomPadding = ResourcesUtils.dp2px(padding.toFloat())
            return this as B
        }

        fun setTitle(title: String?): B {
            buildParams.title = title
            return this as B
        }

        fun setMessage(message: String?): B {
            buildParams.message = message
            return this as B
        }

        fun setImageUrl(imageUrl: String?): B {
            buildParams.imageUrl = imageUrl
            return this as B
        }

        fun setImageUrl(imageUrl: String?, round: Int): B {
            buildParams.imageUrl = imageUrl
            buildParams.round = round
            return this as B
        }

        fun setImageRes(@IdRes imageRes: Int): B {
            buildParams.imageRes = imageRes
            return this as B
        }

        fun setImageRes(@IdRes imageRes: Int, round: Int): B {
            buildParams.imageRes = imageRes
            buildParams.round = round
            return this as B
        }

        fun setDefineText(defineText: String?): B {
            buildParams.defineText = defineText
            return this as B
        }

        fun setCancelText(cancelText: String?): B {
            buildParams.cancelText = cancelText
            return this as B
        }

        fun setCustomView(view: View?): B {
            buildParams.view = view
            return this as B
        }

        fun setDefineClickListener(onClickListener: OnClickListener): B {
            buildParams.defineClickListener = onClickListener
            return this as B
        }

        fun setCancelClickListener(onClickListener: OnClickListener): B {
            buildParams.cancelClickListener = onClickListener
            return this as B
        }

        fun setImageClickListener(onClickListener: OnClickListener): B {
            buildParams.imageClickListener = onClickListener
            return this as B
        }

        open fun build(): D {
            return BaseDialogFragment(context, buildParams) as D
        }
    }

    interface OnClickListener {
        fun onClick(dialog: BaseDialogFragment)
    }
}