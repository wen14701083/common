package com.xhd.android.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by wk on 2021/7/21
 * @param spacing 间隔
 * @param orientation 方向
 * @param includeEdge 是否包含边缘
 */
class ItemDecoration(
    private val spacing: Int,
    private val orientation: Int = LinearLayoutManager.VERTICAL,
    private val includeEdge: Boolean = false
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemCount = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        if (orientation == LinearLayoutManager.VERTICAL) {
            setVertical(outRect, parent, itemCount, position)
        } else {
            setHorizontal(outRect, parent, itemCount, position)
        }
    }

    private fun setHorizontal(outRect: Rect, parent: RecyclerView, itemCount: Int, position: Int) {
        if (includeEdge) {
            if (position == 0) {
                outRect.left = spacing
            } else {
                outRect.left = 0
            }
            outRect.right = spacing
        } else {
            outRect.right = spacing
        }
    }

    private fun setVertical(outRect: Rect, parent: RecyclerView, itemCount: Int, position: Int) {
        if (includeEdge) {
            if (position == 0) {
                outRect.top = spacing
            } else {
                outRect.top = 0
            }
            outRect.bottom = spacing
        } else {
            outRect.bottom = spacing
        }
    }
}
