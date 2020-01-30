package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.CoupleNative
import kekmech.ru.core.dto.Schedule
import kekmech.ru.core.dto.Time
import kekmech.ru.repository.utils.HtmlToScheduleParser
import kekmech.ru.repository.utils.ParserCouple
import kekmech.ru.repository.utils.ParserSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import java.util.*

class LoadScheduleFromRemoteInteractor(val groupNumber: String) : Interactor<Schedule>() {

    override suspend fun onStart(): Schedule {
        if (groupNumber.isEmpty()) throw RuntimeException("Не указан номер текущей группы")
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

        val currentWeekPage = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupNumber)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(true)
            .post()
        // скрапим первую страничку
        val firstWeekHtml = currentWeekPage
            .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
            .html()
        val nextWeekHref = currentWeekPage
            .select("span[class*=mpei-galaktika-lessons-grid-nav]")
            .select("a")
            .last()
            .attr("href")

        val firstWeekSchedule = async { HtmlToScheduleParser().parse(firstWeekHtml) }
        val secondWeekShedule = async {
            // скрапим вторую страничку
            val secondWeekHtml = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/table.aspx$nextWeekHref")
                .get()
                .select("table[class*=mpei-galaktika-lessons-grid-tbl]")
                .html()
            HtmlToScheduleParser().parse(secondWeekHtml)
        }

        // объединяем результаты парсинга
        val joinedCouples = mutableListOf<ParserCouple>()
        joinedCouples.addAll(firstWeekSchedule.await().couples.onEach { couple -> couple.week = 1 })
        joinedCouples.addAll(secondWeekShedule.await().couples.onEach { couple -> couple.week = 2 })
        val finalParserSchedule = ParserSchedule(
            joinedCouples,
            firstMonday.calendar
        )

        return Schedule(
            0,
            groupNumber.toUpperCase(Locale.getDefault()),
            Time.today().weekOfYear,
            Time.today().weekOfSemester, // deprecated
            finalParserSchedule.couples.map {
                CoupleNative(
                    0,
                    it.name,
                    it.teacher,
                    it.place,
                    it.timeStart,
                    it.timeEnd,
                    it.type,
                    it.num,
                    it.day,
                    it.week
                )
            },
            "sch_v2"
        )
    }

    private fun<T> async(action: suspend CoroutineScope.() -> T) = GlobalScope.async(Dispatchers.IO, block = action)

}