package com.xhd.base.utils

import java.security.MessageDigest
import java.util.*

/**
 * Create by wk on 2021/7/5
 */
object EncryptUtils {

    @JvmStatic
    fun md5Encrypt(content: String): String {
        try {
            val digest = MessageDigest.getInstance("MD5").digest(content.toByteArray())
            val sb = StringBuffer()
            for (b in digest) {
                val i = b.toInt() and 0xFF
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    hexString = "0$hexString"
                }
                sb.append(hexString)
            }
            return sb.toString()
        } catch (e: Exception) {
        }
        return ""
    }
}
