package com.xhd.android.base

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.xhd.android.R

/**
 * Create by wk on 2021/7/1
 */
abstract class BaseListFragment<VM : BaseListViewModel, T : Any> : BaseFragment<VM>() {

    var mRvList: RecyclerView? = null
    var mSrlRefresh: SmartRefreshLayout? = null
    val mAdapter by lazy {
        initAdapter()
    }

    abstract fun initAdapter(): BaseQuickAdapter<T, *>

    override fun initView(view: View) {
        mRvList = view.findViewById(R.id.rv_list)
        initList()

        mSrlRefresh = view.findViewById(R.id.srl_refresh)
        initRefresh()
    }

    private fun initRefresh() {
        mSrlRefresh?.let {
            if (this is OnRefreshListener) {
                it.setEnableRefresh(true)
                it.setOnRefreshListener(this)
            } else {
                it.setEnableRefresh(false)
            }

            if (this is OnLoadMoreListener) {
                it.setEnableLoadMore(true)
                it.setOnLoadMoreListener(this)
            } else {
                it.setEnableLoadMore(false)
            }
        }
    }

    private fun initList() {
        mRvList?.let {
            it.adapter = mAdapter
            it.layoutManager = getLayoutManager()
        }
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    fun refreshList() {
        mViewModel.resetCurPage()
        loadData()
    }

    /**
     * 简化adapter参数
     */
    fun setData(
        list: MutableList<T>?,
        desc: String = "暂无数据",
        @DrawableRes src: Int = R.drawable.icon_empty_default,
        showEmpty: Boolean = true
    ) {
        super.setData(this.mAdapter, list, desc, src, showEmpty)
    }

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return mSrlRefresh
    }
}
