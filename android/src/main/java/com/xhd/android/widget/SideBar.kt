package com.xhd.android.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.xhd.android.R
import com.xhd.android.utils.ResourcesUtils

/**
 * Content: 侧边检索A-Z
 * Create by wk on 2021/8/24
 */
class SideBar : View {

    private var onTouchingChangeListener: OnTouchingChangeListener? = null
    private var mSelectedText: TextView? = null // 选中后界面显示控件

    private var mSelectedIndex = -1

    private val mPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = ResourcesUtils.getColor(R.color.C_666666)
            textSize = ResourcesUtils.getDimens(R.dimen.sp_10).toFloat()
        }
    }

    private val letterHeight by lazy {
        mPaint.measureText("A") + ResourcesUtils.getDimens(R.dimen.dp_8)
    }

    private val mLetters: MutableList<String> by lazy {
        val list = mutableListOf<String>()
        list.add("☆")
        val item = 'A'
        for (i in 0..25) {
            list.add((item + i).toString())
        }
        list.add("#")
        list
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val mWidth = ResourcesUtils.getDimens(R.dimen.dp_25)
        val mHeight = (letterHeight * getLettersLength()).toInt()

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until getLettersLength()) {
            val xPos = width / 2 - mPaint.measureText(mLetters[i]) / 2
            val yPos = letterHeight * (i + 1)
            canvas?.drawText(mLetters[i], xPos, yPos, mPaint)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y
        val oldSelected = mSelectedIndex
        val selected = (y / height * getLettersLength()).toInt()

        when (action) {
            MotionEvent.ACTION_UP -> {
                mSelectedIndex = -1
                onTouchingChangeListener?.cancel()
                mSelectedText?.apply {
                    text = ""
                    visibility = GONE
                }
                invalidate()
            }
            else -> {
                if (selected == oldSelected) {
                    return true
                }
                if (selected < 0 || selected >= getLettersLength()) {
                    return true
                }
                val value = mLetters[selected]
                onTouchingChangeListener?.onTouchingChanged(if (selected == 0) "☆热门城市" else value)
                mSelectedText?.apply {
                    text = value
                    visibility = VISIBLE
                }
                mSelectedIndex = selected
                invalidate()
            }
        }
        return true
    }

    private fun getLettersLength(): Int {
        return mLetters.size
    }

    fun setOnTouchingChangeListener(onTouchingChangeListener: OnTouchingChangeListener) {
        this.onTouchingChangeListener = onTouchingChangeListener
    }

    fun setSelectedText(textView: TextView) {
        this.mSelectedText = textView
    }

    interface OnTouchingChangeListener {

        fun onTouchingChanged(value: String)
        fun cancel()
    }
}
