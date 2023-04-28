package com.xhd.android.http

import com.xhd.android.bean.ResultBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @date created on 2023/4/17
 */
suspend inline fun <reified T> Call<T>.await(): T {
    return suspendCoroutine { coroutineContext ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body == null) {
                    coroutineContext.resumeWithException(CustomException("0", "response body为空"))
                    return
                }
                coroutineContext.resume(body)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                coroutineContext.resumeWithException(t)
            }
        })
    }
}
