package cn.xhd.android.common.base.http

import cn.xhd.android.common.UrlConfig
import com.xhd.android.http.RequestLiveData
import com.xhd.android.utils.LogUtils

/**
 * @date created on 2023/4/17
 */
object TmkRequest : RequestLiveData() {
    override fun onError(err: Exception) {
        LogUtils.e("error:" + err)
    }

    override fun getSuccessCode(): String {
        return "200"
    }
}

fun <T> request(block: suspend () -> Result<T>) = TmkRequest.request(block)
