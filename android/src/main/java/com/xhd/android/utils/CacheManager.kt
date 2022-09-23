package com.xhd.android.utils

import android.content.Context
import android.os.Environment

/**
 * Create by wk on 2021/8/20
 */
object CacheManager {

    fun getTotalCacheSize(context: Context): String {
        var size = FileUtils.getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            size += FileUtils.getFolderSize(context.externalCacheDir)
        }
        return FileUtils.getFormatSize(size.toDouble())
    }

    fun cleanCache(context: Context) {
        FileUtils.deleteFolder(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            FileUtils.deleteFolder(context.externalCacheDir)
        }
    }
}
