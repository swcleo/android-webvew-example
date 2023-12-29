package net.front.gt2.webview

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

var LOG_TAG = "WebViewDEV"

class MainActivity : AppCompatActivity() {
    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }

        override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            // This method is called when the page starts loading
            Log.d(LOG_TAG, "Page started loading: $url")
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest
        ): WebResourceResponse? {
            val url = request.url?.toString()
            if (url != null) {
                val cookies: String? = CookieManager.getInstance().getCookie(url)
                Log.d(LOG_TAG, "Cookies for $url: $cookies")
            }
            return super.shouldInterceptRequest(view, request)
        }
    }


    private class MyWebChromeClient : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            Log.d(LOG_TAG, "onConsoleMessage: " + consoleMessage?.message())
            return true
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // chrome://inspect/#devices
        WebView.setWebContentsDebuggingEnabled(true)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.setBackgroundColor(Color.TRANSPARENT)

        Log.i(LOG_TAG, myWebView.settings.userAgentString)

        val sdkVersion = Build.VERSION.SDK_INT
        Log.d(LOG_TAG, "SDK version is: $sdkVersion")

        myWebView.webChromeClient = MyWebChromeClient()  // 新增這一行
        myWebView.webViewClient = MyWebViewClient()

        myWebView.loadUrl("https://www.google.com/")
    }
}

