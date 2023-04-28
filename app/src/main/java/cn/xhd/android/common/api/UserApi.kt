package cn.xhd.android.common.api

import cn.xhd.android.common.base.http.TestServiceCreator
import cn.xhd.android.common.base.http.request
import com.google.gson.annotations.SerializedName
import com.xhd.android.http.await
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @date created on 2023/4/17
 */
interface UserApi {
    // 版本更新
    @GET
    fun getVersionUpdate(@Url url: String?): Call<UpdateBean>
}

data class UpdateBean(
    val content: String,
    @SerializedName("version_code")
    val versionCode: Int,
    @SerializedName("version_name")
    val versionName: String,
    @SerializedName("forced_code")
    val forcedCode: Int = 1,
    @SerializedName("download_url")
    val downloadUrl: String
)

object UserRepository {
    private val userApi = TestServiceCreator.create(UserApi::class.java)

    fun getVersion() = request {
        val content = userApi.getVersionUpdate("https://testtmk.xhd.cn/resource/cc_version.json").await()
        Result.success(content)
    }
}
