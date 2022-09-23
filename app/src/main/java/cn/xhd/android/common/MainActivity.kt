package cn.xhd.android.common

import android.content.Intent
import com.xhd.android.base.BaseActivity
import com.xhd.android.base.SimpleViewModel
import com.xhd.android.utils.setOnDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<SimpleViewModel>() {
    override fun initLayout(): Int {
        return R.layout.activity_main
    }

    override fun initData(intent: Intent) {
    }

    override fun initView() {
        tv_hello.setOnDoubleClickListener {
            mViewModel.startLoading()
        }
    }

    override fun loadData() {
    }

    override fun initObserve() {
    }
}
