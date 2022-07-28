package com.xhd.base.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xhd.base.base.BaseViewModel
import com.xhd.base.bean.LoadingState
import java.lang.reflect.ParameterizedType

/**
 * Create by wk on 2021/7/2
 */
abstract class BaseViewModelDialog<VM : BaseViewModel>: BaseDialog {

    private var mLoadingDialog: DialogLoading ?= null
    constructor() : super()
    constructor(context: Context) : super(context)

    val mViewModel : VM by lazy {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cla = parameterizedType.actualTypeArguments[0] as Class<VM>
        ViewModelProvider(this).get(cla)
    }

    override fun onStart() {
        super.onStart()
        val lp = mWindow?.attributes
        lp?.dimAmount = 0.5f
        mWindow?.attributes = lp
        mWindow?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun initGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun initView(view: View) {
        if (mContext != null) {
            mLoadingDialog = DialogLoading.Builder(mContext!!).build()
        }
        loadData()
        initObserve()
        loadingObserver()
    }

    /**
     * initView之后调用，在这里可以用ViewModel请求网络数据
     */
    abstract fun loadData()

    /**
     *  lavaData与view关链
     */
    abstract fun initObserve()

    fun showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing()) {
            mLoadingDialog?.show()
        }
    }

    fun stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
        }
    }

    fun <T> addObserver(liveData: LiveData<Result<T>>, block: (T?) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer {
            mViewModel.finishLoading()
            if (it is Result<T>) {
                if (it.isSuccess) {
                    block(it.getOrNull())
                }
            }
        })
    }

    fun <T> addObserver(liveData: LiveData<Result<T>>, failure: ()-> Unit, block: (T?)-> Unit) {
        liveData.observe(viewLifecycleOwner) {
            mViewModel.finishLoading()
            if (it is Result<T>) {
                if (it.isSuccess) {
                    block(it.getOrNull())
                } else {
                    failure()
                }
            }
        }
    }

    private fun loadingObserver() {
        mViewModel.mLoadStatusLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is LoadingState.LoadingStart -> showLoading()
                is LoadingState.LoadingFinish -> stopLoading()
            }
        }
    }
}