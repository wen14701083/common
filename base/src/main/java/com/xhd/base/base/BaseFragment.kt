package com.xhd.base.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xhd.base.annotation.BindEventBus
import com.xhd.base.base.action.ViewAction
import com.xhd.base.dialog.DialogLoading
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * Create by wk on 2021/7/1
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment(), ViewAction {

    lateinit var mContext: Context

    companion object {

        fun addNewTaskFlag(intent: Intent): Intent {
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private val mLoadingDialog: DialogLoading by lazy {
        createDialogLoading()
    }

    @Suppress("UNCHECKED_CAST")
    val mViewModel: VM by lazy {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cla = parameterizedType.actualTypeArguments[0] as Class<VM>
        ViewModelProvider(this)[cla]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(initLayout(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(arguments)
        initView(view)
        loadData()
        initObserve()
        loadingObserver()

        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().register(this)
        }
    }

    // ******************************* ViewAction start *******************************
    override fun getViewModel(): BaseViewModel {
        return mViewModel
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return this
    }

    override fun getLoadingDialog(): DialogLoading {
        return mLoadingDialog
    }

    override fun getContext(): Context {
        return mContext
    }

    override fun getRefreshLayout(): SmartRefreshLayout? = null

    override fun createDialogLoading(): DialogLoading {
        return DialogLoading.Builder(mContext).build()
    }

    // ******************************* ViewAction end *******************************

    override fun onDestroy() {
        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    abstract fun initLayout(): Int

    /**
     * 生命周期开始,在initView之前调用，主要用于初始化数据
     */
    abstract fun initData(arguments: Bundle?)

    /**
     * initData之后调用，主要是初始化View
     */
    abstract fun initView(view: View)

    /**
     * initView之后调用，在这里可以用ViewModel请求网络数据
     */
    abstract fun loadData()
}
