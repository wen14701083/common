package com.xhd.android.http

import androidx.lifecycle.liveData
import com.xhd.android.bean.HttpResult
import kotlinx.coroutines.Dispatchers

/**
 * @date created on 2023/4/17
 */
abstract class RequestLiveData {

    fun <T> request(block: suspend () -> Result<T>) = liveData(Dispatchers.IO) {
        val result = try {
            val result = block()
            val finallyResult = if (result.isSuccess) {
                val body = result.getOrNull()
                if (body is HttpResult) {
                    if (body.code == getSuccessCode()) {
                        result
                    } else {
                        Result.failure(CustomException(body.code, body.msg))
                    }
                } else {
                    result
                }
            } else {
                result
            }
            finallyResult
        } catch (err: Exception) {
            onError(err)
            Result.failure(err)
        }
        emit(result)
    }

    abstract fun onError(err: Exception)

    abstract fun getSuccessCode(): String
}
