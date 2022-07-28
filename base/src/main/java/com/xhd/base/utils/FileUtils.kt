package com.xhd.base.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import com.xhd.base.base.BaseApplication
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigDecimal

/**
 * Create by wk on 2021/7/23
 */
object FileUtils {

    const val SIZE_KB = 1024
    const val SIZE_MB = 1024 * SIZE_KB

    /**
     * 获取图片缓存目录路径
     *
     * @return 图片缓存目录路径
     */
    fun getImagePath(): String? {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
    }

    fun getVideoPath(): String? {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_MOVIES)?.absolutePath
    }

    fun getAudioPath(): String? {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath
    }

    fun getFilePath(): String? {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath
    }

    fun getDownloadPath(): String? {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
    }

    fun getLogPath(): String {
        return BaseApplication.context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath.toString() + "/log"
    }

    fun exists(path: String?): Boolean {
        if (TextUtils.isEmpty(path)) {
            return false
        }
        return File(path!!).exists()
    }

    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (file.exists()) {
            return true
        }
        return try {
            file.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun writeFileFromIS(filePath: String, input: InputStream?): Boolean {
        return writeFileFromIS(File(filePath), input, false)
    }

    fun writeFileFromIS(file: File, input: InputStream?, append: Boolean): Boolean {
        if (!createOrExistsFile(file) || input == null) {
            return false
        }
        var output: OutputStream? = null
        try {
            output = BufferedOutputStream(FileOutputStream(file, append))
            val byteArray = ByteArray(SIZE_KB)
            var len: Int
            while (input.read(byteArray, 0, SIZE_KB).also { len = it } != -1) {
                output.write(byteArray, 0, len)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                input.close()
                output?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getBitmap(imgPath: String): Bitmap {
        val newOpts = BitmapFactory.Options()
        newOpts.inJustDecodeBounds = false
        newOpts.inPurgeable = true
        newOpts.inInputShareable = true
        newOpts.inSampleSize = 1
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565 // 设置RGB
        return BitmapFactory.decodeFile(imgPath, newOpts)
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte: Double = size / SIZE_KB
        if (kiloByte < 1) {
            return size.toString() + "B"
        }
        val megaByte: Double = kiloByte / SIZE_KB
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "K"
        }
        val gigaByte: Double = megaByte / SIZE_KB
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "M"
        }
        val teraBytes: Double = gigaByte / SIZE_KB
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return (result4.setScale(1, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB")
    }

    fun deleteFolder(file: File?) {
        try {
            file?.let {
                if (it.isDirectory) {
                    val listFiles = it.list()
                    listFiles?.forEach { childer ->
                        deleteFolder(File(it, childer))
                    }
                } else {
                    it.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFolderSize(file: File?): Long {
        var size = 0L
        try {
            val files = file?.listFiles()
            files?.forEach {
                size += if (it.isDirectory) {
                    getFolderSize(it)
                } else {
                    it.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     *  压缩图片到1M
     */
    fun compressAndGenImage(imgPath: String): File? {
        try {
            val bitmap = getBitmap(imgPath)
            val degree = readPictureDegree(imgPath)
            val resultBitmap = rotatingImageView(degree, bitmap)
            val os = ByteArrayOutputStream()
            var options = 100
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, options, os)
            while (os.toByteArray().size / SIZE_KB > SIZE_KB) {
                os.reset()
                options -= 10
                if (options < 0) {
                    break
                }
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, options, os)
            }

            val outPath = "${getImagePath()}/image.png"
            val fos = FileOutputStream(outPath)
            fos.write(os.toByteArray())
            fos.flush()

            os.close()
            fos.close()

            val result = File(outPath)
            if (result.exists()) {
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     *  获取图片旋转角度
     */
    fun readPictureDegree(path: String): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            degree = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return degree
    }

    /**
     * 旋转图片
     */
    fun rotatingImageView(angle: Int, bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun file2Base64(imgPath: String?): String {
        try {
            val input = FileInputStream(imgPath)
            val byteArray = ByteArray(input.available())
            input.read(byteArray)
            input.close()
            return "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun downloadFile(
        file: File,
        response: Response<ResponseBody>,
        downloadListener: DownloadListener
    ) {
        ktxRunOnBgSingle {
            response.body()?.let { it ->
                val input = it.byteStream()
                val totalLength = it.contentLength()
                if (!createOrExistsFile(file)) {
                    return@ktxRunOnBgSingle
                }
                var out: OutputStream? = null
                var currentLength = 0
                try {
                    out = BufferedOutputStream(FileOutputStream(file))
                    val data = ByteArray(1024)
                    var len = 0
                    while (input.read(data, 0, 1024).also { len = it } != -1) {
                        out.write(data, 0, len)
                        currentLength += len
                        val progress = (100 * currentLength / totalLength).toInt()
                        downloadListener.onProgress(progress)
                    }
                    downloadListener.onSuccess(file.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    downloadListener.onError(e.message)
                } finally {
                    try {
                        input.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        out?.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun saveLocalImage(context: Context, resId: Int) {
        try {
            var bitmap = BitmapFactory.decodeResource(context.resources, resId)
            val file = File("${getImagePath()}/save_${System.currentTimeMillis()}.png")
            val output = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            output.flush()
            output.close()
            // 回收图片
            if (!bitmap.isRecycled) {
                bitmap.recycle()
                bitmap = null
            }
            // 同步到相册
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(File(file.absolutePath))
            intent.data = contentUri
            context.sendBroadcast(intent)
            toast("保存成功")
        } catch (e: Exception) {
            toast("保存失败，请稍后重试")
        }
    }
}
