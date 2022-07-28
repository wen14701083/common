package com.xhd.base.utils

import android.os.Build
import android.text.Html
import java.text.DecimalFormat

/**
 * 保留两位小数
 */
fun Double.decimalPlaces2(): String = DecimalFormat("#0.00").format(this)

/**
 * 转为人民币
 */
fun Double.toRMB(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml("&yen", Html.FROM_HTML_MODE_COMPACT).toString() + this.decimalPlaces2()
    } else {
        Html.fromHtml("&yen").toString() + this.decimalPlaces2()
    }
}
