package com.xhd.base.utils

import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toFile
import com.xhd.base.base.BaseApplication
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * @date created on 2022/7/20
 */
object UriUtils {

    @JvmStatic
    fun getFilePath(uri: Uri): String {
        val file = uri2File(uri) ?: return ""
        return file.absolutePath
    }

    fun uri2File(uri: Uri): File? {
        val path = uri.path ?: return null
        var file: File? = null

        when (uri.scheme) {
            ContentResolver.SCHEME_FILE -> {
                file = uri.toFile()
            }
            ContentResolver.SCHEME_CONTENT -> {
                var input: InputStream? = null
                try {
                    val context = BaseApplication.context
                    file = File(context.cacheDir, System.currentTimeMillis().toString())
                    input = context.contentResolver.openInputStream(uri)
                    val bytes = input?.readBytes() ?: return null
                    file.writeBytes(bytes)
                } catch (exception: FileNotFoundException) {
                    exception.printStackTrace()
                    return null
                } finally {
                    runCatching {
                        input?.close()
                    }.onFailure {
                        it.printStackTrace()
                    }
                }
            }
        }
        return file
    }
}
