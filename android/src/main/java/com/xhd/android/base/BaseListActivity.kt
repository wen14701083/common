package com.xhd.android.base

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.xhd.android.R

/**
 * Create by wk on 2021/6/29
 */
abstract class BaseListActivity<VM : BaseListViewModel, T : Any> : BaseActivity<VM>() {

    var mRvList: RecyclerView? = null
    private var mSrlRefresh: SmartRefreshLayout? = null
    val mAdapter by lazy {
        initAdapter()
    }

    abstract fun initAdapter(): BaseQuickAdapter<T, *>

    override fun initView() {
        mRvList = findViewById(R.id.rv_list)
        initList()

        mSrlRefresh = findViewById(R.id.srl_refresh)
        initRefresh()
    }

    /**
     * 初始化刷新组件，实现接口
     * SmartRefreshLayout @+id=R.id.srl_refresh
     */
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

    /**
     * 初始化list
     * RecyclerView @+id=R.id.rv_list
     */
    private fun initList() {
        mRvList?.let {
            it.adapter = mAdapter
            it.layoutManager = getLayoutManager()
        }
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
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
        desc: String = Global.mEmptyDesc,
        @DrawableRes src: Int = Global.mEmptyIcon,
        showEmpty: Boolean = true
    ) {
        super.setData(this.mAdapter, list, desc, src, showEmpty)
    }

    override fun getRefreshLayout(): SmartRefreshLayout? {
        return mSrlRefresh
    }
}
