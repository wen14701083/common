package com.xhd.android.widget

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.xhd.android.utils.LogUtils
import com.xhd.android.utils.PublicUtils

/**
 * Create by wk on 2021/7/20
 */
class CustomWebView(private val mContext: Context, attr: AttributeSet?) : WebView(mContext, attr) {

    var progressBar: ProgressBar? = null
    var webListener: WebListener? = null

    constructor(context: Context) : this(context, null)

    init {
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            defaultTextEncodingName = "UTF-8"
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            // 设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；
            // 当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，
            // 无法提供一个宽度值，这个时候该方法将被使用。
            useWideViewPort = true
            // 缩放至屏幕的大小
            loadWithOverviewMode = true
            // 设置允许JS弹窗
            javaScriptCanOpenWindowsAutomatically = true
            setSupportZoom(true)
            builtInZoomControls = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }

        setViewClient()
        setChromeClient()
    }

    fun setJsInterface(js: JsInterface) {
        addJavascriptInterface(js, "android")
    }

    private fun setChromeClient() {
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }

            override fun onReceivedTitle(view: WebView, title: String?) {
                webListener?.onReceivedTitle(view, title)
                super.onReceivedTitle(view, title)
            }
        }
    }

    private fun setViewClient() {
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar?.let {
                    it.visibility = View.VISIBLE
                    it.progress = 0
                }
                webListener?.onPageStart(url)
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressBar?.visibility = View.GONE
                webListener?.onPageFinished(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                LogUtils.e("scheme ${Uri.parse(url).scheme}, url $url")
                if (PublicUtils.isHttp(url)) {
                    view.loadUrl(url)
                }
                return true
            }

            @TargetApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                webListener?.onPageError(view, error?.description?.toString())
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                webListener?.onPageError(view, description)
            }
        }
    }

    class JsInterface(context: Context) {

        @JavascriptInterface
        fun test() {
        }
    }

    interface WebListener {

        fun onPageStart(url: String?)
        fun onPageError(view: WebView, error: String?)
        fun onPageFinished(url: String)
        fun onReceivedTitle(view: WebView, title: String?)
    }
}
