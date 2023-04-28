package cn.xhd.android.common.base.http

import cn.xhd.android.common.UrlConfig
import com.xhd.android.http.ServiceCreator
import okhttp3.OkHttpClient

/**
 * @date created on 2023/4/17
 */
object TestServiceCreator : ServiceCreator() {
    override fun getBaseUrl(): String {
        return UrlConfig.BASE_URL
    }

    override fun addInterceptor(builder: OkHttpClient.Builder) {
        super.addInterceptor(builder)
    }
}
