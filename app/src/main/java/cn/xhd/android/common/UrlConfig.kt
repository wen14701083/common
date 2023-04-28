package cn.xhd.android.common

/**
 * @date created on 2023/4/17
 */
object UrlConfig {
    private val isDebug = BuildConfig.DEBUG

    val BASE_URL = if (!isDebug) {
        "https://tmk.xhd.cn/api/"
    } else {
        "https://testtmk.xhd.cn/api/"
    }
}
