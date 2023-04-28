package cn.xhd.android.common.base

import com.hjq.bar.TitleBar
import com.xhd.android.base.BaseApplication

/**
 * @date created on 2023/4/12
 */
class NCApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        TitleBar.setDefaultStyle(TitleStyle())
    }
}
