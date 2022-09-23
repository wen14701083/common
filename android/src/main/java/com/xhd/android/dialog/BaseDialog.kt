package com.xhd.android.dialog

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import com.xhd.android.R
import com.xhd.android.base.action.ActivityAction

/**
 * @date created on 2022/9/21
 */
open class BaseDialog constructor(
    context: Context,
    @StyleRes themeResId: Int = R.style.BaseDialogTheme
) : AppCompatDialog(context, themeResId) {

    /**
     * 获取 Dialog 的根布局
     */
    open fun getContentView(): View? {
        val contentView: View? = findViewById(Window.ID_ANDROID_CONTENT)
        if (contentView is ViewGroup && contentView.childCount == 1) {
            return contentView.getChildAt(0)
        }
        return contentView
    }

    /**
     * 设置 Dialog 宽度
     */
    open fun setWidth(width: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.width = width
        window.attributes = params
    }

    /**
     * 设置 Dialog 高度
     */
    open fun setHeight(height: Int) {
        val window: Window = window ?: return
        val params: WindowManager.LayoutParams? = window.attributes
        params?.height = height
        window.attributes = params
    }

    /**
     * 设置 Dialog 重心
     */
    open fun setGravity(gravity: Int) {
        window?.setGravity(gravity)
    }

    override fun dismiss() {
        super.dismiss()
    }

    open class Builder<B : Builder<B>> constructor(private val context: Context) : ActivityAction {
        private var dialog: BaseDialog? = null
        var contentView: View? = null

        /** 主题样式 */
        private var themeId: Int = R.style.BaseDialogTheme

        /** 宽度和高度 */
        private var width: Int = WindowManager.LayoutParams.WRAP_CONTENT
        private var height: Int = WindowManager.LayoutParams.WRAP_CONTENT

        /** 重心位置 */
        private var gravity: Int = Gravity.CENTER

        /** 是否能够被取消 */
        private var cancelable: Boolean = true

        /** 点击空白是否能够取消  前提是这个对话框可以被取消 */
        private var canceledOnTouchOutside: Boolean = false

        /** 背景遮盖层开关 */
        private var backgroundDimEnabled: Boolean = true

        /** 背景遮盖层透明度 */
        private var backgroundDimAmount: Float = 0.5f

        /**
         * 设置布局
         */
        open fun setContentView(@LayoutRes id: Int): B {
            // 这里解释一下，为什么要传 new FrameLayout，因为如果不传的话，XML 的根布局获取到的 LayoutParams 对象会为空，也就会导致宽高参数解析不出来
            return setContentView(
                LayoutInflater.from(context).inflate(id, FrameLayout(context), false)
            )
        }

        open fun setContentView(view: View?): B {
            // 请不要传入空的布局
            if (view == null) {
                throw IllegalArgumentException("are you ok?")
            }
            contentView = view
            if (isCreated()) {
                dialog?.setContentView(view)
                return this as B
            }
            val layoutParams: ViewGroup.LayoutParams? = contentView?.layoutParams
            if ((layoutParams != null) && (width == ViewGroup.LayoutParams.WRAP_CONTENT) && (height == ViewGroup.LayoutParams.WRAP_CONTENT)) {
                // 如果当前 Dialog 的宽高设置了自适应，就以布局中设置的宽高为主
                setWidth(layoutParams.width)
                setHeight(layoutParams.height)
            }
            setGravity(gravity)
            return this as B
        }

        /**
         * 设置宽度
         */
        open fun setWidth(width: Int): B {
            this.width = width
            if (isCreated()) {
                dialog?.setWidth(width)
                return this as B
            }
            return this as B
        }

        /**
         * 设置高度
         */
        open fun setHeight(height: Int): B {
            this.height = height
            if (isCreated()) {
                dialog?.setHeight(height)
                return this as B
            }
            return this as B
        }

        /**
         * 设置重心位置
         */
        open fun setGravity(gravity: Int): B {
            this.gravity = gravity
            if (isCreated()) {
                dialog?.setGravity(gravity)
            }
            return this as B
        }

        /**
         * 是否可以取消
         */
        open fun setCancelable(cancelable: Boolean): B {
            this.cancelable = cancelable
            if (isCreated()) {
                dialog?.setCancelable(cancelable)
            }
            return this as B
        }

        /**
         * 是否可以通过点击空白区域取消
         */
        open fun setCanceledOnTouchOutside(cancel: Boolean): B {
            canceledOnTouchOutside = cancel
            if (isCreated() && cancelable) {
                dialog?.setCanceledOnTouchOutside(cancel)
            }
            return this as B
        }

        /**
         * 创建
         */
        open fun create(): BaseDialog {
            // 判断布局是否为空
            if (contentView == null) {
                throw IllegalArgumentException("are you ok?")
            }
            if (isShowing()) {
                dismiss()
            }

            dialog = createDialog(context, themeId)
            dialog!!.let { dialog ->
                dialog.setContentView(contentView!!)
                dialog.setCancelable(cancelable)
                if (cancelable) {
                    dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
                }
                val window: Window? = dialog.window
                if (window != null) {
                    val params: WindowManager.LayoutParams = window.attributes
                    params.width = width
                    params.height = height
                    params.gravity = gravity

                    if (backgroundDimEnabled) {
                        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                        window.setDimAmount(backgroundDimAmount)
                    } else {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    }
                    window.attributes = params
                }
            }
            return dialog!!
        }

        /**
         * 创建 Dialog 对象（子类可以重写此方法来改变 Dialog 类型）
         */
        protected open fun createDialog(context: Context, @StyleRes themeId: Int): BaseDialog {
            return BaseDialog(context, themeId)
        }

        /**
         * 显示
         */
        open fun show() {
            if (!isCreated()) {
                create()
            }
            if (isShowing()) {
                return
            }
            dialog?.show()
        }

        /**
         * 当前 Dialog 是否显示了
         */
        open fun isShowing(): Boolean {
            return isCreated() && dialog!!.isShowing
        }

        /**
         * 当前 Dialog 是否创建了
         */
        open fun isCreated(): Boolean {
            return dialog != null
        }

        /**
         * 销毁当前 Dialog
         */
        open fun dismiss() {
            val activity = getActivity()
            if (activity == null || activity.isFinishing || activity.isDestroyed) {
                return
            }
            dialog?.dismiss()
        }

        override fun getContext(): Context {
            return context
        }

        /**
         * 获取当前 Dialog 对象
         */
        open fun getDialog(): BaseDialog? {
            return dialog
        }
    }

    interface OnClickListener {
        fun onClick(dialog: BaseDialog?)
    }
}
