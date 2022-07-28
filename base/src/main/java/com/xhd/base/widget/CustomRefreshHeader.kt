package com.xhd.base.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent
import com.xhd.base.R
import com.xhd.base.utils.ResourcesUtils

/**
 * Content: 刷新头部
 * Create by wk on 2021/6/29
 */
class CustomRefreshHeader(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SimpleComponent(context, attrs, defStyleAttr), RefreshHeader {

    private var ivIcon: ImageView
    private var tvText: TextView
    private var pbLoading: ProgressBar

    companion object {

        const val DELAY_TIME = 500
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null)
        ivIcon = view.findViewById(R.id.iv_icon)
        tvText = view.findViewById(R.id.tv_text)
        pbLoading = view.findViewById(R.id.pb_loading)

        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ResourcesUtils.getDimens(R.dimen.dp_40)
        )
        addView(view, params)
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        tvText.text = resources.getString(R.string.refresh_finished)
        stopLoading()
        return DELAY_TIME
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    override fun isSupportHorizontalDrag(): Boolean = false
    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
        when (newState) {
            RefreshState.PullDownToRefresh -> {
                tvText.text = ResourcesUtils.getString(R.string.refresh_pull_down)
                ivIcon.setImageResource(R.drawable.icon_down_refresh)
                ivIcon.visibility = View.VISIBLE
            }
            RefreshState.ReleaseToRefresh -> {
                tvText.text = ResourcesUtils.getString(R.string.refresh_release_to)
                ivIcon.setImageResource(R.drawable.icon_up_refresh)
                ivIcon.visibility = View.VISIBLE
            }
            RefreshState.Loading -> {
                tvText.text = ResourcesUtils.getString(R.string.refreshing)
                startLoading()
            }
            else -> {}
        }
    }

    private fun startLoading() {
        ivIcon.visibility = View.GONE
        pbLoading.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        ivIcon.visibility = View.GONE
        pbLoading.visibility = View.GONE
    }
}
