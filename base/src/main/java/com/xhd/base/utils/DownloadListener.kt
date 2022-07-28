package com.xhd.base.utils

/**
 * Create by wk on 2021/9/7
 */
interface DownloadListener {

    fun onProgress(progress: Int)
    fun onError(message: String?)
    fun onSuccess(filePath: String?)
}
