package com.xhd.base.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.xhd.base.R

/**
 * Create by wk on 2021/7/21
 * 每行之间包含横向分割线
 * @param spanCount 行数
 * @param spacing 间隔
 */
class GridItemHasLineDecoration(
    private val spanCount: Int,
    private val spacing: Int
) : RecyclerView.ItemDecoration() {

    private val mPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = ResourcesUtils.getColor(R.color.C_ECECEC)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            if (childCount - i > spanCount && i % spanCount == 0) {
                c.drawRect(
                    spacing.toFloat(),
                    child.bottom.toFloat() + spacing,
                    parent.right.toFloat() - spacing,
                    child.bottom.toFloat() + spacing + 1f,
                    mPaint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // 根据position确定列数
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount
        outRect.top = spacing
        outRect.bottom = spacing
    }
}
