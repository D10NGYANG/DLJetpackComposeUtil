package com.d10ng.basicjetpackcomposeapp.view

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(
    modifier: Modifier = Modifier,
    url: String,
    onPageFinished: (String) -> Unit = {},
    onReceivedTitle: (String) -> Unit = {},
    onProgressChanged: (Float) -> Unit = {}
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val webView = WebView(context)
            webView.settings.apply {
                // 启用JS
                javaScriptEnabled = true
                // 设置布局绘制算法
                layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                // 支持<meta>标签的viewport属性
                useWideViewPort = true
                // 支持缩放
                setSupportZoom(true)
                // 使用DOM存储API
                domStorageEnabled = true
                // 解决部分照片不显示的问题
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    url?.apply(onPageFinished)
                }
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    title?.apply(onReceivedTitle)
                }
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    onProgressChanged(newProgress / 100f)
                }
            }
            webView.loadUrl(url)
            webView
        })
}

@Preview
@Composable
private fun WebView_Test() {
    WebView(
        modifier = Modifier.fillMaxSize(),
        url = "https://www.beidoueyes.com"
    )
}