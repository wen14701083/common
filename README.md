自用项目基础框架，kotlin+mvvm
#更新时间
2023年4月12日
 - 1、添加基础说明步骤

#1、导入依赖
`implementation 'cn.xhd.common.android:base:0.0.12'`

#2、maven依赖
`maven { url 'https://jitpack.io' }
maven { url 'https://repo1.maven.org/maven2/' }
maven {
    allowInsecureProtocol=true
    url 'http://nexus.xhd.cn:8081/repository/maven-public/'
}
google()
mavenCentral()`

#3、gradle.properties
`android.enableJetifier=true`

#4、继承BaseApplication

 - 初始化title
   TitleStyle 继承 CommonBarStyle
   TitleBar.setDefaultStyle(TitleStyle())

 - 初始化LogUtils
   LogUtils.enable(BuildConfig.DEBUG)

 - http相关
```
object TestServiceCreator : ServiceCreator() {
    override fun getBaseUrl(): String {
        return UrlConfig.BASE_URL
    }

    override fun addInterceptor(builder: OkHttpClient.Builder) {
        super.addInterceptor(builder)
    }
}
```

```
object TmkRequest : RequestLiveData() {
    override fun onError(err: Exception) {
        LogUtils.e("error:" + err)
    }

    override fun getSuccessCode(): String {
        return "200"
    }
}

fun <T> request(block: suspend () -> Result<T>) = TmkRequest.request(block)
```
