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
class WebkitLatch(private val webView: WebView) {
    @Volatile private lateinit var countDownLatch: CountDownLatch
    @Volatile private lateinit var url: String

    init {
        webView.post {
            url = webView.originalUrl ?: ""
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

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (this@WebkitLatch.url != url.toString()) {
                        this@WebkitLatch.url = url.toString()
                        countDownLatch.countDown()
                    }
                }
            }
        }
    }

    suspend fun async(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        observer: WebView.() -> Unit
    ) = withContext(coroutineContext) {
        countDownLatch = CountDownLatch(1)
        webView.post { observer(webView) }
        countDownLatch.await()
        url
    }
}