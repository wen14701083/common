package com.xhd.base.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.xhd.base.R

/**
 * Create by wk on 2021/6/29
 */

abstract class BaseDialog : DialogFragment {
    var mWindow : Window ?= null
    var mRootView : View ?= null
    var mContext: Context ?= null

    constructor(): super()
    constructor(context: Context) {
        this.mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext?.let {
            mRootView = LayoutInflater.from(it).inflate(attachLayoutRes(), null)
        }
        return mRootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        initWindow()
        mRootView?.let {
            initView(it)
        }
    }

    private fun initWindow() {
        mWindow = dialog?.window
        mWindow?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mWindow?.setGravity(initGravity())
        if (initDialogAnimation() != -1) {
            mWindow?.setWindowAnimations(initDialogAnimation())
        }
    }

    protected open fun cancelOrShowDialogAnimation(show: Boolean) {
        if (show) {
            if (initDialogAnimation() != -1) {
                mWindow?.setWindowAnimations(initDialogAnimation())
            }
        } else {
            mWindow?.setWindowAnimations(R.style.dialog_animation_empty)
        }

    }

    /**
     * 设置dialog进入退出动画
     *
     * @return
     */
    @StyleRes
    protected open fun initDialogAnimation(): Int {
        return -1
    }

    protected abstract fun attachLayoutRes() : Int

    protected abstract fun initGravity(): Int

    protected abstract fun initView(view: View)

    open fun initDialog() {
        dialog?.setCanceledOnTouchOutside(false)
        isCancelable = true
    }

    open fun isShowing() : Boolean {
        return isAdded && dialog != null && dialog!!.isShowing
    }

    fun show() {
        if (mContext is AppCompatActivity) {
            show((mContext as AppCompatActivity).supportFragmentManager, "dialog")
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val clazz = Class.forName("androidx.fragment.app.DialogFragment")
            val con = clazz.getConstructor()
            val obj = con.newInstance()
            val dismissed = clazz.getDeclaredField("mDismissed")
            dismissed.isAccessible = true
            dismissed[obj] = false
            val shownByMe = clazz.getDeclaredField("mShownByMe")
            shownByMe.isAccessible = true
            shownByMe[obj] = false
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        try {
            dismissAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}