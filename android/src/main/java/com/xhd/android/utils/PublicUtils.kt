package com.xhd.android.utils

import android.os.Build
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Create by wk on 2021/7/1
 */
object PublicUtils {

    // 判断没有中文
    fun noChinese(str: String?, start: Int, end: Int): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val p = Pattern.compile("^[^\\u4e00-\\u9fa5]*{$start,$end}$")
        return p.matcher(str!!).matches()
    }

    // 判断表情
    fun hasEmoji(str: String?): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val p = Pattern.compile("[^\\u0020-\\u007E\\u00A0-\\u00BE\\u2E80-\\uA4CF\\uF900-\\uFAFF\\uFE30-\\uFE4F\\uFF00-\\uFFEF\\u0080-\\u009F\\u2000-\\u201f\r\n]")
        return p.matcher(str!!).find()
    }

    // 判断手机格式是否正确
    fun isPhone(phone: String?): Boolean {
        if (TextUtils.isEmpty(phone)) {
            return false
        }
        val p = Pattern.compile("^1\\d{10}$")
        return p.matcher(phone!!).matches()
    }

    // 判断是否是网址
    fun isHttp(url: String?): Boolean {
        if (TextUtils.isEmpty(url)) {
            return false
        }
        val pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://).*")
        return pattern.matcher(url!!).matches()
    }

    // 富文本显示
    fun webHtml(content: String?): String {
        return """
            <!DOCTYPE HTML>
            <html>
                <head>
                    <style>
                        img{ max-width:100%;width:100%;max-height:100%;height:100%;}
                        *{word-break:break-all;}
                    </style>
                </head>
                <body> $content </body>
            </html>
        """.trimIndent()
    }

    fun isAndroidQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 获取富文本纯文字
     */
    fun getTextForHtml(html: String?): String {
        if (TextUtils.isEmpty(html)) {
            return ""
        }
        return html!!.replace(Regex("<.+?>"), "")
    }
}
