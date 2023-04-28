package cn.xhd.android.common

import android.content.Context
import android.content.Intent
import android.view.Gravity
import cn.xhd.android.common.base.dialog.MyDialog
import cn.xhd.android.common.test.TestActivity
import com.xhd.android.base.BaseActivity
import com.xhd.android.base.SimpleViewModel
import com.xhd.android.dialog.DefaultDialog
import com.xhd.android.ktx.startActivity
import com.xhd.android.utils.LogUtils
import com.xhd.android.utils.setOnDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<SimpleViewModel>() {
    companion object {
        fun start(context: Context) {
            startActivity(context, MainActivity::class.java) {
                putExtra("id", "main")
            }
        }
    }

    override fun initStatusBar(isDark: Boolean) {
        setFullStatusBar()
    }

    override fun initLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        LogUtils.enable(true)
        tv_hello.setOnDoubleClickListener {
            MyDialog.Builder(this)
                .setTitle("哈哈哈")
                .setMessage("这是一个提示消息")
                .setCanceledOnTouchOutside(true)
                .setPadding(30, 0, 30, 0)
                .setGravity(Gravity.CENTER)
                .setCancelClickListener { dialog -> dialog?.dismiss() }
                .setDefineClickListener { dialog -> dialog?.dismiss() }
                .setDefine("确定吗")
                .setCancel("哈的")
                .show()
//            TestActivity.start(this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        LogUtils.e(intent?.getStringExtra("id") ?: "main error")
    }

    override fun loadData() {

    }

    override fun initObserve() {
    }
}
