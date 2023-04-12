Kotlin + MVVM

1、导入依赖
implementation 'cn.xhd.common.android:base:0.0.12'

2、maven依赖
maven { url 'https://jitpack.io' }
maven { url 'https://repo1.maven.org/maven2/' }
maven {
    allowInsecureProtocol=true
    url 'http://nexus.xhd.cn:8081/repository/maven-public/'
}
google()
mavenCentral()

3、gradle.properties
android.enableJetifier=true

4、继承BaseApplication
LogUtils.enable(BuildConfig.DEBUG)


