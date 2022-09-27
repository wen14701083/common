package com.xhd.android.http

import com.xhd.android.BuildConfig
import com.xhd.android.utils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class ServiceCreator() {
    companion object {

        const val TIMEOUT = 30L
    }

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor()
    }

    private val retrofit by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(defaultClient)
            .build()
    }

    private val defaultClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        val builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(
                // 添加日志拦截器
                if (LogUtils.debug) {
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
        addInterceptor(builder)
        builder.build()
    }

    abstract fun getBaseUrl(): String
    open fun addInterceptor(builder: OkHttpClient.Builder) {
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}
