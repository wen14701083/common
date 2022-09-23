package com.xhd.android.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.xhd.android.R
import com.xhd.android.utils.ResourcesUtils

/**
 * Content: 导航页标识点
 * Create by wk on 2021/7/1
 */
class GuideDotView(
    context: Context,
    attr: AttributeSet,
    defStyleAttr: Int
) : LinearLayout(context, attr, defStyleAttr) {

    private var dotCount: Int = 0
    private var selectedColor: Int = ResourcesUtils.getColor(R.color.colorPrimary)
    private var defaultColor: Int = ResourcesUtils.getColor(R.color.black)
    private var dotMargin: Int = ResourcesUtils.dp2px(3f)
    private var dotWidth: Int = ResourcesUtils.dp2px(10f)
    private var dotHeight: Int = ResourcesUtils.dp2px(10f)
    private var dotRadius: Int = ResourcesUtils.dp2px(5f)

    constructor(context: Context, attr: AttributeSet) : this(context, attr, 0) {
        val array = context.obtainStyledAttributes(attr, R.styleable.GuideDotView)
        dotCount = array.getInt(R.styleable.GuideDotView_count, 0)
        selectedColor = array.getColor(R.styleable.GuideDotView_selected_color, ResourcesUtils.getColor(R.color.colorPrimary))
        defaultColor = array.getColor(R.styleable.GuideDotView_default_color, ResourcesUtils.getColor(R.color.black))
        dotMargin = array.getDimensionPixelSize(R.styleable.GuideDotView_dot_margin, ResourcesUtils.dp2px(3f))
        dotWidth = array.getDimensionPixelSize(R.styleable.GuideDotView_dot_width, ResourcesUtils.dp2px(10f))
        dotHeight = array.getDimensionPixelSize(R.styleable.GuideDotView_dot_height, ResourcesUtils.dp2px(10f))
        dotRadius = array.getDimensionPixelSize(R.styleable.GuideDotView_dot_radius, ResourcesUtils.dp2px(5f))
        initView()
        array.recycle()
    }

    private fun initView() {
        if (dotCount <= 0) {
            return
        }
        removeAllViews()
        for (i in 0 until dotCount) {
            val dot = RoundTextView(context)
            val params = LayoutParams(dotWidth, dotHeight)
            params.setMargins(dotMargin, 0, dotMargin, 0)
            dot.delegate.backgroundColor = if (i == 0) {
                selectedColor
            } else {
                defaultColor
            }
            dot.delegate.cornerRadius = dotRadius
            dot.layoutParams = params
            addView(dot)
        }
    }

    fun setSelectedCount(count: Int) {
        if (count < 0 || count > childCount) {
            return
        }
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is RoundTextView) {
                view.delegate.backgroundColor = if (i == count) {
                    selectedColor
                } else {
                    defaultColor
                }
            }
        }
    }

    fun setCount(count: Int) {
        if (count > 0) {
            this.dotCount = count
            initView()
        }
    }
}
