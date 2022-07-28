package com.xhd.base.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.xhd.base.R
import com.xhd.base.utils.LogUtils

/**
 * Content: 评分控件自定义
 * Create by wk on 2019/7/19
 */
class RatingView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : View(context, attrs, defStyleAttr) {

    private var ratingDistance = 0 // 间距
    private var ratingWidth = 0 // 单个宽度
    private var ratingHeight = 0 // 单个高度
    private var ratingPaddingLeft = 0
    private var ratingPaddingRight = 0
    private var ratingPaddingTop = 0
    private var ratingPaddingBottom = 0
    private var ratingSun = 0 // 评分图形个数
    private var isSupportHalf = false // 是否支持半颗星
    private var totalScore = 0 // 总评分
    var currentRating = 0 // 当前分数

    private var emptyDrawable: Drawable? = null
    private var fillDrawable: Drawable? = null
    private var halfDrawable: Drawable? = null

    private var emptyBitmap: Bitmap? = null
    private var fillBitmap: Bitmap? = null
    private var halfBitmap: Bitmap? = null

    private var fillPaint: Paint? = null // 绘制星星画笔
    private var halfPaint: Paint? = null
    private var emptyPaint: Paint? = null
    private var change = false // 是否支持修改

    private var onRatingChangeListener: OnRatingChangeListener? = null

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        val mTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RatingView)
        ratingDistance = mTypedArray.getDimension(R.styleable.RatingView_distance, 0f).toInt()
        ratingWidth = mTypedArray.getDimension(R.styleable.RatingView_rating_width, 0f).toInt()
        ratingHeight = mTypedArray.getDimension(R.styleable.RatingView_rating_height, 0f).toInt()
        ratingPaddingLeft = mTypedArray.getDimension(R.styleable.RatingView_rating_padding_left, 0f)
            .toInt()
        ratingPaddingRight = mTypedArray.getDimension(R.styleable.RatingView_rating_padding_right, 0f)
            .toInt()
        ratingPaddingTop = mTypedArray.getDimension(R.styleable.RatingView_rating_padding_top, 0f)
            .toInt()
        ratingPaddingBottom = mTypedArray.getDimension(R.styleable.RatingView_rating_padding_bottom, 0f)
            .toInt()
        totalScore = mTypedArray.getDimension(R.styleable.RatingView_rating_total_score, 5f).toInt()
        ratingSun = mTypedArray.getInteger(R.styleable.RatingView_rating_sum, 5)
        isSupportHalf = mTypedArray.getBoolean(R.styleable.RatingView_rating_support_half, false)
        emptyDrawable = mTypedArray.getDrawable(R.styleable.RatingView_rating_un_star_img)
        halfDrawable = mTypedArray.getDrawable(R.styleable.RatingView_rating_half_star_img)
        fillDrawable = mTypedArray.getDrawable(R.styleable.RatingView_rating_star_img)
        mTypedArray.recycle()

        emptyBitmap = drawableToBitmap(emptyDrawable)
        halfBitmap = drawableToBitmap(halfDrawable)
        fillBitmap = drawableToBitmap(fillDrawable)

        emptyPaint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(emptyBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        fillPaint = Paint().apply {
            isAntiAlias = true
            shader = BitmapShader(fillBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        if (isSupportHalf) {
            halfPaint = Paint().apply {
                isAntiAlias = true
                shader = BitmapShader(halfBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            ratingWidth * ratingSun + ratingDistance * (ratingSun - 1) + ratingPaddingLeft + ratingPaddingRight,
            ratingHeight + ratingPaddingTop + ratingPaddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (null == fillDrawable || null == emptyDrawable) {
            LogUtils.e("must set rating_un_star_img and rating_star_img of xml.")
            return
        }

        // 绘制空星星
        for (i in 0 until ratingSun) {
            emptyDrawable!!.setBounds(
                ratingPaddingLeft + (ratingDistance + ratingWidth) * i,
                ratingPaddingTop,
                ratingPaddingLeft + (ratingDistance + ratingWidth) * i + ratingWidth,
                ratingPaddingTop + ratingHeight
            )
            emptyDrawable!!.draw(canvas)
        }
        if (currentRating <= 0) {
            return
        }
        var halfCount = 0
        if (isSupportHalf) {
            halfCount = if (currentRating % 2 == 0) 0 else 1
        }

        // 绘制有满星
        val fillCount: Int = (currentRating / 2)
        for (i in 0 until fillCount) {
            fillDrawable!!.setBounds(
                ratingPaddingLeft + (ratingDistance + ratingWidth) * i,
                ratingPaddingTop,
                ratingPaddingLeft + (ratingDistance + ratingWidth) * i + ratingWidth,
                ratingPaddingTop + ratingHeight
            )
            fillDrawable!!.draw(canvas)
        }
        // 绘制半颗星
        if (halfCount == 1) {
            halfDrawable!!.setBounds(
                ratingPaddingLeft + (ratingDistance + ratingWidth) * fillCount,
                ratingPaddingTop,
                ratingPaddingLeft + (ratingDistance + ratingWidth) * fillCount + ratingWidth,
                ratingPaddingTop + ratingHeight
            )
            halfDrawable!!.draw(canvas)
        }
    }

    var oldRating = 0 // 上次评分
    var downClick = 0 // 点击星颗数
    var countClick = 0 // 点击星次数
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (change) {
            return true
        }
        var x = event.x.toInt()
        var rating = 0
        val measuredWidth = measuredWidth.toFloat()
        if (x > measuredWidth) {
            x = measuredWidth.toInt()
        }
        rating = getRating(x.toFloat(), measuredWidth)
        val clickRating = getClickRating(x.toFloat(), measuredWidth)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (downClick != clickRating) {
                    countClick = 0
                }
                downClick = clickRating
                if (oldRating != rating) {
                    oldRating = rating
                    setCurrent(rating)
                }
            }
            MotionEvent.ACTION_MOVE -> if (oldRating != rating) {
                oldRating = rating
                setCurrent(rating)
            }
            MotionEvent.ACTION_UP -> if (downClick == clickRating) {
                setRating(clickRating)
            } else {
                countClick = 0
            }
            else -> {}
        }
        return true
    }

    private fun setRating(count: Int) {
        if (!isSupportHalf) {
            setCurrent(count)
            return
        }
        val min = (count - 1) * 2
        val max = count * 2
        when (countClick) {
            0 -> {
                countClick++
                setCurrent(max)
            }
            1 -> {
                countClick++
                setCurrent(min + 1)
            }
            else -> {
                setCurrent(min)
                countClick = 0
            }
        }
    }

    /**
     * 根据点击位置获取绘制分数
     * @param x             所处x坐标
     * @param measuredWidth 控件宽度
     * @return 分数
     */
    private fun getRating(x: Float, measuredWidth: Float): Int {
        if (x >= measuredWidth) {
            return MAX_RATING
        }
        val fillCount = (x / (ratingWidth + ratingDistance)).toInt()
        val halfCount = if (x - (ratingWidth + ratingDistance) * fillCount - ratingWidth / 2 >= 0) 1 else 0
        return fillCount * 2 + halfCount
    }

    private fun getClickRating(x: Float, measuredWidth: Float): Int {
        if (x >= measuredWidth) {
            return ratingSun
        }
        val width = ratingWidth + ratingDistance
        if (x > 0 && x < width) {
            return 1
        } else if (x > width && x < 2 * width) {
            return 2
        } else if (x > 2 * width && x < 3 * width) {
            return 3
        } else if (x > 3 * width && x < 4 * width) {
            return 4
        } else if (x > 4 * width && x < 5 * width) {
            return 5
        }
        return ratingSun
    }

    fun change(change: Boolean) {
        this.change = change
    }

    fun setOnRatingChangeListener(listener: OnRatingChangeListener?) {
        onRatingChangeListener = listener
    }

    /**
     * 获取当前评分
     * @return 当前评分
     */
    fun getCurrent(): Int {
        return if (isSupportHalf) {
            currentRating
        } else {
            currentRating / 2
        }
    }

    fun setCurrent(rating: Int) {
        currentRating = if (!isSupportHalf) {
            2 * rating
        } else {
            rating
        }
        if (onRatingChangeListener != null) {
            onRatingChangeListener!!.ratingChange(getCurrent())
        }
        invalidate()
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        return if (drawable == null) {
            null
        } else Bitmap.createBitmap(ratingWidth, ratingHeight, Bitmap.Config.ARGB_8888)
    }

    interface OnRatingChangeListener {
        fun ratingChange(currentRating: Int)
    }

    companion object {
        const val MAX_RATING = 10 // 最大评分为10，后续根据totalScore计算实际分数
    }
}
