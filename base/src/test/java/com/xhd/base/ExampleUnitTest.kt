package com.xhd.base

import com.xhd.base.utils.AESUtils
import com.xhd.base.utils.EncryptUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testAes() {
        val s = "a123456"
        val encryptResultStr = EncryptUtils.encrypt(s)
        println(encryptResultStr)
    }
}