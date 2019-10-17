package kekmech.ru.addscreen.presenter

import kekmech.ru.addscreen.parser.HtmlToScheduleParser
import kekmech.ru.addscreen.parser.ParserCouple
import kekmech.ru.addscreen.parser.ParserSchedule
import kekmech.ru.core.dto.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URI

class HTMLWorker {

    private fun formSubmitScript(group: String) =
        "document.getElementsByName('$TEXTBOX_NAME')[0].value = '$group';" +
                "document.getElementsByName('$BUTTON_NAME')[0].click();"

    private fun getHtmlScript() = "document.documentElement.outerHTML"

    private fun timetableUrl(groupId: String, startDate: Time) =
        "$MPEI_TIMETABLE?groupoid=$groupId&start=${startDate.formattedAsYearMonthDay}"

    suspend fun tryGroupAsync(group: String) = GlobalScope.async(Dispatchers.IO) {
        // загружаем страничку и вбиваем номер группы в форму
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!

        // вычисляем первый понедельник семестра и второй понедельник семестра
        val firstMonday = Time.firstSemesterDay().gotoMonday()
        val secondMonday = firstMonday.getDayWithOffset(7)

        val href = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), group)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(false)
            .post()
            .select("a[href]")
            .first()
            .attr("href")

        val groupId = href
            .let { URI.create(it).query }
            .substringAfter("=")

        // загружаем первый
        withContext(Dispatchers.IO) {
            // скрапим первую страничку
            val firstWeekHtml = Jsoup.connect("$href&start=${firstMonday.formattedAsYearMonthDay}")
                .get()
                .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                .html()
            // скрапим вторую страничку
            val secondWeekHtml = Jsoup.connect("$href&start=${secondMonday.formattedAsYearMonthDay}")
                .get()
                .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                .html()

            Pair(firstWeekHtml, secondWeekHtml)
        }.let {
            // парсим обе странички
            Pair(
                HtmlToScheduleParser().parse(/*StringEscapeUtils.unescapeJava*/(it.first)),
                HtmlToScheduleParser().parse(/*StringEscapeUtils.unescapeJava*/(it.second))
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