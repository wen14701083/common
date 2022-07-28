package com.xhd.base.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.TitleBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.xhd.base.R
import com.xhd.base.annotation.BindEventBus
import com.xhd.base.base.action.TitleAction
import com.xhd.base.base.action.ViewAction
import com.xhd.base.dialog.DialogLoading
import com.xhd.base.utils.LogUtils
import com.xhd.base.utils.singClick
import org.greenrobot.eventbus.EventBus
import java.lang.reflect.ParameterizedType

/**
 * @date created on 2022/7/13
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), ViewAction, TitleAction {

    lateinit var mActivity: BaseActivity<VM>

    /** 标题栏对象 */
    private var titleBar: TitleBar? = null

    companion object {

        fun addNewTaskFlag(context: Context, intent: Intent): Intent {
            return if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            } else {
                intent
            }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayout())
        mActivity = this
        LogUtils.d(localClassName)
        titleBar = getTitleBar()
        titleBar?.setOnTitleBarListener(this)
        initStatusBar()
        initData(intent)
        initView()
        loadData()
        initObserve()
        loadingObserver()

        if (javaClass.isAnnotationPresent(BindEventBus::class.java)) {
            EventBus.getDefault().register(this)
        }
    }

    // ******************************* TitleAction start *******************************
    override fun onLeftClick(titleBar: TitleBar?) {
        singClick {
            onBackPressed()
        }
    }

    override fun getTitleBar(): TitleBar? {
        if (titleBar == null) {
            titleBar = obtainTitleBar(getContentView())
        }
        return titleBar
    }
    // ******************************* TitleAction end *******************************

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
        return this
    }

    override fun getRefreshLayout(): SmartRefreshLayout? = null

    override fun createDialogLoading(): DialogLoading {
        return DialogLoading.Builder(this).build()
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
    abstract fun initData(intent: Intent)

    /**
     * initData之后调用，主要是初始化View
     */
    abstract fun initView()

    /**
     * initView之后调用，在这里可以用ViewModel请求网络数据
     */
    abstract fun loadData()

    open fun initStatusBar() { // 深色状态栏文字
        immersionBar {
            fitsSystemWindows(true)
            statusBarDarkFont(true)
            statusBarColor(R.color.C_F9F9F9)
        }
    }

    /**
     * 和 setContentView 对应的方法
     */
    open fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }

    protected fun addFragment(@IdRes contentViewId: Int, fragment: Fragment) {
        runCatching {
            supportFragmentManager.commit(true) {
                replace(contentViewId, fragment, fragment.javaClass.simpleName)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    protected fun removeFragment(fragment: Fragment) {
        runCatching {
            supportFragmentManager.commit(true) {
                remove(fragment)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}
