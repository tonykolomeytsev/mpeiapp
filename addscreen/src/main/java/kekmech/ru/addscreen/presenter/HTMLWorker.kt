package kekmech.ru.addscreen.presenter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kekmech.ru.addscreen.parser.HtmlToScheduleParser
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringEscapeUtils

class HTMLWorker(private val webView: WebView) {

    private lateinit var group: String
    private lateinit var parser: HtmlToScheduleParser
    private lateinit var web: Wrapper

    suspend fun tryGroup(group: String) {
        this.group = group
        GlobalScope.launch(Dispatchers.IO) {
            web = Wrapper(webView)
            web.load(MPEI_DEFAULT)
            fillInputs()

            var html = ""
            var running = true
            while (web.urls.size <= 3 && running) {
                if (web.urls.size == 3) {
                    if (web.urls.any { it.contains("groupoid") }) {
                        print("GROUPOID")
                        web.js("document.documentElement.outerHTML") { html = it }
                        while (html.isEmpty()) { delay(10) } // дождаться загрузки странички
                        running = false
                    } else throw RuntimeException("Не удалось получить страничку")
                }
                println("SIZE " + web.urls.size.toString())
            }

            val contains = html.contains("mpei-galaktika-lessons-grid-tbl")
            if (!contains) throw IllegalArgumentException("Неправильный номер группы")

            val schedule = HtmlToScheduleParser().parse(StringEscapeUtils.unescapeJava(html))
            print(schedule.couples.joinToString("\n"))

        }
    }

    private fun fillInputs() {
        web.lock()
        web.js("document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';") {}
        web.js("document.getElementsByName('$BUTTON_NAME')[0].click();") {}
    }

    @SuppressLint("SetJavaScriptEnabled")
    class Wrapper(private val webView: WebView) {
        @Volatile
        var locker = false
        @Volatile
        var url = ""
        @Volatile
        var urls = mutableListOf<String>()

        fun lock() {
            locker = true
        }

        fun unlock() {
            locker = false
        }

        init {
            webView.post {
                webView.settings.apply {
                    javaScriptEnabled = true
                    setSupportMultipleWindows(false)
                    loadsImagesAutomatically = false
                    defaultTextEncodingName = "utf-8"
                }
                webView.webViewClient = object : WebViewClient() {
                    var weekNum = 1

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        view?.loadUrl(request?.url.toString())
                        return false
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        lock()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        unlock()
                        println("onPageFinished " + url!!)
                        urls.add(url)
                        this@Wrapper.url = url.toString()
                    }
                }
            }
        }

        fun post(action: () -> Unit) = webView.post(action)

        fun js(script: String, lock: Boolean = false, callback: ((String) -> Unit)?) {
            if (lock) locker = true
            post { webView.evaluateJavascript(script, callback) }
        }

        suspend fun awaitSync() {
            while (GlobalScope.async(Dispatchers.IO) { locker }.await());
        }

        suspend fun load(url: String) {
            this.url = ""
            locker = true
            post { webView.loadUrl(url) }
            awaitSync()
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