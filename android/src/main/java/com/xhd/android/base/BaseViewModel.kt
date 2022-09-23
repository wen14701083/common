package com.xhd.android.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xhd.android.bean.LoadingState

/**
 * Create by wk on 2021/6/29
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    val mLoadStatusLiveData: MutableLiveData<LoadingState> by lazy { MutableLiveData<LoadingState>() }
    val mRefreshLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun startLoading() {
        mLoadStatusLiveData.postValue(LoadingState.LoadingStart)
    }

    fun finishLoading() {
        mLoadStatusLiveData.postValue(LoadingState.LoadingFinish)
    }

    open fun refresh() {
        mRefreshLiveData.value = true
    }
}
