package cn.xhd.android.common.base.dialog

import android.content.Context
import cn.xhd.android.common.R
import com.xhd.android.dialog.DefaultDialog

/**
 * @date created on 2023/4/27
 */
class MyDialog {

    class Builder(context: Context) : DefaultDialog.Builder(context) {
        override fun getContentView(): Int {
            return R.layout.dialog_default_tmk
        }
    }
}
