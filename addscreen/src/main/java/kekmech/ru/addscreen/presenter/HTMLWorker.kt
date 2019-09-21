package kekmech.ru.addscreen.presenter

import android.webkit.WebView
import kekmech.ru.addscreen.parser.HtmlToScheduleParser
import kekmech.ru.addscreen.parser.ParserCouple
import kekmech.ru.addscreen.parser.ParserSchedule
import kekmech.ru.core.dto.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.apache.commons.text.StringEscapeUtils

class HTMLWorker(private val webView: WebView) {

    private fun formSubmitScript(group: String) =
        "document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';" +
                "document.getElementsByName('$BUTTON_NAME')[0].click();"

    private fun getHtmlScript() = "document.documentElement.outerHTML"

    private fun timetableUrl(groupId: String, startDate: Time) =
        "$MPEI_TIMETABLE?groupoid=$groupId&start=${startDate.formattedAsYearMonthDay}"

    suspend fun tryGroupAsync(group: String) = GlobalScope.async(Dispatchers.IO) {
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
            joinedCouples.addAll(it.second.couples.onEach { couple -> couple.week = 2 })
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

    companion object {
        const val MPEI_DEFAULT = "https://mpei.ru/Education/timetable/Pages/default.aspx"
        const val MPEI_TIMETABLE = "https://mpei.ru/Education/timetable/Pages/table.aspx"
        const val TEXTBOX_NAME = "ctl00\$ctl30\$g_f0649160_e72e_4671_a36b_743021868df5\$ctl03"
        const val BUTTON_NAME =  "ctl00\$ctl30\$g_f0649160_e72e_4671_a36b_743021868df5\$ctl04"
    }
}