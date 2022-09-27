package cn.xhd.android.common

import com.xhd.android.base.BaseActivity
import com.xhd.android.base.SimpleViewModel
import com.xhd.android.utils.setOnDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<SimpleViewModel>() {
    override fun initLayout(): Int {
        return R.layout.activity_main
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
