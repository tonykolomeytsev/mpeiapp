package kekmech.ru.addscreen.presenter

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch
import kotlin.coroutines.CoroutineContext

@SuppressLint("SetJavaScriptEnabled")
class WebkitLatchJs(private val webView: WebView) {
    private lateinit var countDownLatch: CountDownLatch

    init {
        webView.post {
            webView.settings.apply {
                javaScriptEnabled = true
                setSupportMultipleWindows(false)
                loadsImagesAutomatically = false
                defaultTextEncodingName = "utf-8"
            }
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(request?.url.toString())
                    return false
                }
            }
        }
    }

    suspend fun async(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        script: String
    ) = withContext(coroutineContext) {
        countDownLatch = CountDownLatch(1)
        var result = ""
        webView.post {
            webView.evaluateJavascript(script) {
                result = it
                countDownLatch.countDown()
            }
        }
        countDownLatch.await()
        result
    }
}