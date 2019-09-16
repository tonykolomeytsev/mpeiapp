package kekmech.ru.addscreen.presenter

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kekmech.ru.addscreen.parser.HtmlToScheduleParser

class HTMLWorker(webView: WebView) {

    private lateinit var group: String
    private lateinit var parser: HtmlToScheduleParser
    private val web = Wrapper(webView)

    @SuppressLint("SetJavaScriptEnabled")
    suspend fun tryGroup(group: String) = web.post {
        this.group = group
        web.settings.apply {
            javaScriptEnabled = true
            setSupportMultipleWindows(false)
            loadsImagesAutomatically = false
        }
        web.webViewClient = object : WebViewClient() {
            var weekNum = 1

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (url?.contains("default.aspx") == true) {
                    fillInputs()
                } else if (url?.contains("table.aspx") == true) {
                    getHtml(weekNum++, url.toString())
                }
            }
        }
        webView.loadUrl(MPEI_DEFAULT)
    }

    fun fillInputs() {
        web.js("document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';", null)
        web.js("document.getElementsByName('$BUTTON_NAME')[0].click();", null)
    }

    fun getHtml(week: Int, url: String) {
        web.js("document.innerHtml") {
            println(it)

            if (week == 1) web.loadUrl(MPEI_TIMETABLE + "?groupoid=")
        }
    }

    class Wrapper(private val webView: WebView) {
        fun post(action: () -> Unit) = webView.post(action)

        fun js(script: String, callback: ((String) -> Unit)?) = webView.evaluateJavascript(script, callback)
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