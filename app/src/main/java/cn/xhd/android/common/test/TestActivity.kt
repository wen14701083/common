package cn.xhd.android.common.test

import android.content.Context
import cn.xhd.android.common.MainActivity
import cn.xhd.android.common.R
import com.xhd.android.base.BaseActivity
import com.xhd.android.ktx.startActivity
import com.xhd.android.utils.LogUtils
import com.xhd.android.utils.setOnDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @date created on 2022/9/30
 */
class TestActivity : BaseActivity<TestViewModel>() {
    companion object {
        fun start(context: Context) {
            startActivity(context, TestActivity::class.java) {
                putExtra("id", "test")
            }
        }
    }

    override fun initLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        tv_hello.text = "test"
        tv_hello.setOnDoubleClickListener {
            MainActivity.start(this)
        }
        LogUtils.e(intent?.getStringExtra("id") ?: "test error")
    }

    override fun loadData() {
        mViewModel.refresh()
    }

    override fun initObserve() {
        addNormalObserver(mViewModel.versionLiveData) {
            if (it.isSuccess) {
                LogUtils.e("xxxxx${it.getOrNull()}")
            } else {
                LogUtils.e("xxxxx2222$it")
            }
        }
    }
}
