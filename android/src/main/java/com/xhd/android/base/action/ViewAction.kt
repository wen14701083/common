package com.xhd.android.base.action

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xhd.android.R
import com.xhd.android.base.BaseListViewModel
import com.xhd.android.base.BaseViewModel
import com.xhd.android.bean.LoadingState
import com.xhd.android.bean.ResultListBean
import com.xhd.android.dialog.BaseDialog

/**
 * Activity 和 Fragment 统一数据处理方法
 * @date created on 2022/7/13
 */
interface ViewAction {

    fun getViewModel(): BaseViewModel

    fun getLifecycleOwner(): LifecycleOwner

    fun getLoadingDialog(): BaseDialog

    fun getContext(): Context

    fun getRefreshLayout(): SmartRefreshLayout?

    fun createDialogLoading(): BaseDialog

    /**
     *  lavaData与view关链
     */
    fun initObserve()

    /**
     *  显示loading
     */
    private fun showLoading() {
        val mLoadingDialog = getLoadingDialog()
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show()
        }
    }

    /**
     *  取消loading
     */
    private fun stopLoading() {
        val mLoadingDialog = getLoadingDialog()
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss()
        }
    }

    /**
     * loading状态观察
     */
    fun loadingObserver() {
        addNormalObserver(getViewModel().mLoadStatusLiveData) {
            when (it) {
                is LoadingState.LoadingStart -> showLoading()
                is LoadingState.LoadingFinish -> stopLoading()
            }
        }
    }

    /**
     * 非网络请求的LiveData
     */
    fun <T> addNormalObserver(liveData: LiveData<T>, block: (T) -> Unit) {
        liveData.observe(getLifecycleOwner()) {
            block(it)
        }
    }

    /**
     * BaseActivity 统一 addObserver
     */
    fun <T> addObserver(
        liveData: LiveData<Result<T>>,
        error: () -> Unit = {},
        block: (T) -> Unit
    ) {
        liveData.observe(getLifecycleOwner()) {
            getViewModel().finishLoading()
            if (it.isSuccess) {
                it.getOrNull()?.let { result ->
                    block(result)
                }
            } else {
                error()
            }
        }
    }

    /**
     *  针对list返回数据的处理
     *  包括错误页或空页面展示
     *  adapter的自动赋值等
     */
    fun <T> addListObserver(
        adapter: BaseQuickAdapter<T, *>,
        liveData: LiveData<Result<ResultListBean<T>>>,
        desc: String = "暂无数据",
        @DrawableRes scr: Int = R.drawable.icon_empty_default,
        block: (T) -> Unit = {}
    ) {
        liveData.observe(getLifecycleOwner()) {
            val viewModel = getViewModel()
            viewModel.finishLoading()
            if (viewModel is BaseListViewModel) {
                setData(adapter, it.getOrNull()?.data, desc, scr)
            } else {
                setListOrEmpty(adapter, it.getOrNull()?.data, desc, scr)
            }
        }
    }

    /**
     * 列表页面自动添加分页数据
     */
    fun <T> setData(
        adapter: BaseQuickAdapter<T, *>,
        list: MutableList<T>?,
        desc: String = "暂无数据",
        @DrawableRes src: Int = R.drawable.icon_empty_default,
        showEmpty: Boolean = true
    ) {
        val viewModel = getViewModel()
        // 不是ListViewModel
        // 没有接入加载更多
        // adapter数据为空且list为空
        if (viewModel !is BaseListViewModel || getRefreshLayout() == null || (list?.isEmpty() == true && adapter.data.isEmpty())) {
            if (showEmpty) {
                setListOrEmpty(adapter, list, desc, src)
            } else {
                adapter.setList(list)
            }
            finishRefreshOrLoad()
            return
        }
        if (viewModel.isFirstPage()) {
            adapter.setNewInstance(list)
        } else {
            adapter.addData(list ?: mutableListOf())
        }
        viewModel.curPage++
        if (viewModel.isLoadEnd(list)) {
            getRefreshLayout()?.finishRefreshWithNoMoreData()
        }
        finishRefreshOrLoad()
    }

    /**
     * 取消刷新和加载组件
     */
    private fun finishRefreshOrLoad() {
        getRefreshLayout()?.apply {
            if (isLoading) {
                finishLoadMore()
            }
        }?.run {
            if (isRefreshing) {
                finishRefresh()
            }
        }
    }

    /**
     * list为空时显示空页面
     */
    @SuppressLint("InflateParams", "NotifyDataSetChanged")
    fun <T> setListOrEmpty(
        adapter: BaseQuickAdapter<T, *>,
        list: MutableList<T>?,
        desc: String = "暂无数据",
        @DrawableRes src: Int = R.drawable.icon_empty_default
    ) {
        if (list != null && list.isNotEmpty()) {
            adapter.setList(list)
        } else {
            val emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_list, null)
            emptyView?.let {
                it.findViewById<TextView>(R.id.tv_empty).text = desc
                it.findViewById<ImageView>(R.id.iv_empty).setImageResource(src)
                adapter.data.clear()
                adapter.setEmptyView(it)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
