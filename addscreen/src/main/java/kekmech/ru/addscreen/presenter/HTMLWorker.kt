package kekmech.ru.addscreen.presenter

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kekmech.ru.core.ASYNCIO
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class HTMLWorker(val webView: WebView) {

    private lateinit var group: String
    private val client = Client(this)

    @SuppressLint("SetJavaScriptEnabled")
    suspend fun tryGroup(group: String) = webView.post {
        this.group = group
        webView.settings.apply {
            javaScriptEnabled = true
            setSupportMultipleWindows(false)
            loadsImagesAutomatically = false
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
        webView.webChromeClient = client
        webView.loadUrl(MPEI_DEFAULT)
    }

    fun fillInputs() {
        println("inputs")
        webView.evaluateJavascript("document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';", null)
        webView.evaluateJavascript("document.getElementsByName('$BUTTON_NAME')[0].click();", null)
    }

    class Client(val worker: HTMLWorker) : WebChromeClient() {
        var progress = LOADING_DEFAULT

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress == 100) {
                when(progress) {
                    LOADING_DEFAULT -> {
                        worker.fillInputs()
                    }
                    else -> LOADING_DEFAULT
                }
            }
        }
    }

    companion object {
        const val MPEI_DEFAULT = "https://mpei.ru/Education/timetable/Pages/default.aspx"
        const val MPEI_TIMETABLE = "https://mpei.ru/Education/timetable/Pages/table.aspx"
        const val TEXTBOX_NAME = "ctl00\$ctl30\$g_f0649160_e72e_4671_a36b_743021868df5\$ctl03"
        const val BUTTON_NAME =  "ctl00\$ctl30\$g_f0649160_e72e_4671_a36b_743021868df5\$ctl04"

        const val LOADING_DEFAULT = 0
        const val FILLING = 1
    }
}