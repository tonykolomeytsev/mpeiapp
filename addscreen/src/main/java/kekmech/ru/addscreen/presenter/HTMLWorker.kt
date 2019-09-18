package kekmech.ru.addscreen.presenter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kekmech.ru.addscreen.parser.HtmlToScheduleParser
import kekmech.ru.addscreen.parser.ParserCouple
import kekmech.ru.addscreen.parser.ParserSchedule
import kekmech.ru.core.dto.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringEscapeUtils

class HTMLWorker(private val webView: WebView) {

    private lateinit var group: String
    private lateinit var parser: HtmlToScheduleParser
    private lateinit var web: Wrapper

    private fun formSubmitScript(group: String) =
        "document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';" +
                "document.getElementsByName('$BUTTON_NAME')[0].click();"

    private fun getHtmlScript() = "document.documentElement.outerHTML"

    private fun timetableUrl(groupId: String, startDate: Time) =
        "$MPEI_TIMETABLE?groupoid=$groupId&start=${startDate.formattedAsYearMonthDay}"

    suspend fun tryGroup(group: String) = GlobalScope.async(Dispatchers.IO) {
        // загружаем страничку и вбиваем номер группы в форму
        WebkitLatch(webView).async { loadUrl(MPEI_DEFAULT) }
        val groupId = WebkitLatch(webView).async {
            evaluateJavascript(formSubmitScript(group), null)
        }.getGroupId()

        // вычисляем первый понедельник семестра и второй понедельник семестра
        val firstMonday = Time.firstSemesterDay().gotoMonday()
        val secondMonday = firstMonday.getDayWithOffset(7)

        // загружаем первый
        withContext(Dispatchers.IO) {
            // скрапим первую страничку
            WebkitLatch(webView).async { loadUrl(timetableUrl(groupId, firstMonday)) }
            val firstWeekHtml = WebkitLatchJs(webView).async(script = getHtmlScript())
            // скрапим вторую страничку
            WebkitLatch(webView).async { loadUrl(timetableUrl(groupId, secondMonday)) }
            val secondWeekHtml = WebkitLatchJs(webView).async(script = getHtmlScript())

            Pair(firstWeekHtml, secondWeekHtml)
        }.let {
            // парсим обе странички
            Pair(
                HtmlToScheduleParser().parse(StringEscapeUtils.unescapeJava(it.first)),
                HtmlToScheduleParser().parse(StringEscapeUtils.unescapeJava(it.second))
            )
        }.let {
            // объединяем результаты парсинга
            val joinedCouples = mutableListOf<ParserCouple>()
            joinedCouples.addAll(it.first.couples)
            joinedCouples.addAll(it.second.couples.onEach { it.week = 2 })
            ParserSchedule(
                joinedCouples,
                firstMonday.calendar
            )
        }
    }

    private fun Regex.findGroupsIn(group: String) = this
        .find(group)
        .let { it?.groups?.toMutableList() }!!
        .filterNotNull()

    private fun String.getGroupId() = "id=(\\d+)".toRegex()
        .findGroupsIn(this)
        .first { it.value.matches("\\d+".toRegex()) }
        .value

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
        const val BUTTON_NAME = "ctl00\$ctl30\$g_f0649160_e72e_4671_a36b_743021868df5\$ctl04"

        const val LOADING_DEFAULT = 0
        const val FILLING = 1
    }
}