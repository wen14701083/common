package com.xhd.android.base

/**
 * Create by wk on 2021/6/29
 */
abstract class BaseListViewModel : BaseViewModel() {

    private var pageSize = 20
    private val startPage = 1
    var curPage = startPage

    fun isLoadEnd(list: List<*>?): Boolean {
        return list == null || list.size < pageSize
    }

    fun resetCurPage() {
        curPage = startPage
    }

    fun setPageSize(size: Int) {
        pageSize = size
    }

    fun isFirstPage(): Boolean {
        return curPage == startPage
    }

    fun getStart(): Int {
        return (curPage - 1) * pageSize
    }

    override fun refresh() {
        resetCurPage()
        super.refresh()
    }
}
