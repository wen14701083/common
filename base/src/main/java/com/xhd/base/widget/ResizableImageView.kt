package com.xhd.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.ceil

/**
 * Content: 宽度全屏-高度自适应ImageView
 * Create by wk on 2021/7/1
 */
class ResizableImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val drawable = drawable
        if (drawable != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = ceil((width.toDouble()) * (drawable.intrinsicHeight.toDouble()) / (drawable.intrinsicWidth.toDouble())).toInt()
            setMeasuredDimension(width, height)
            scaleType = ScaleType.FIT_START
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
