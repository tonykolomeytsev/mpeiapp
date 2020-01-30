package kekmech.ru.repository.gateways

import kekmech.ru.core.dto.AcademicSession
import kekmech.ru.repository.utils.SessionParser
import org.jsoup.Jsoup

class LoadSessionFromRemoteInteractor(val groupNumber: String) : Interactor<AcademicSession>() {

    override suspend fun onStart(): AcademicSession {
        if (groupNumber.isEmpty()) throw RuntimeException("Не задан номер группы")
        val inputs = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .get()
            .select("input")
        val eventValidationInput = inputs.find { it.attr("name") == "__EVENTVALIDATION" }!!
        val viewStateInput = inputs.find { it.attr("name") == "__VIEWSTATE" }!!
        val groupNameInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl03".toRegex()) }!!
        val groupSubmitInput = inputs.find { it.attr("name").matches("ctl00\\\$ctl30.*ctl04".toRegex()) }!!


        val timetable = Jsoup.connect("https://mpei.ru/Education/timetable/Pages/default.aspx")
            .data(eventValidationInput.attr("name"), eventValidationInput.attr("value"))
            .data(viewStateInput.attr("name"), viewStateInput.attr("value"))
            .data(groupNameInput.attr("name"), groupNumber)
            .data(groupSubmitInput.attr("name"), groupSubmitInput.attr("value"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .followRedirects(true)
            .post()

        val __EVENTVALIDATION = timetable.select("input[name=__EVENTVALIDATION]").attr("value")
        val __VIEWSTATE = timetable.select("input[name=__VIEWSTATE]").attr("value")
        val __EVENTARGUMENT = "0"
        val __VIEWSTATEGENERATOR = timetable.select("input[name=__VIEWSTATEGENERATOR]").attr("value")
        val __EVENTTARGET = timetable.select("div[class=mpei-tt-outer-wrap]").select("a").attr("href").let {
            ".*'(.*)'.*'.*'.*".toRegex().find(it)?.groups?.get(1)?.value ?: ""
        }
        val result = Jsoup.connect(timetable.location())
            .data("__EVENTVALIDATION", __EVENTVALIDATION)
            .data("__VIEWSTATE", __VIEWSTATE)
            .data("__EVENTARGUMENT", __EVENTARGUMENT)
            .data("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR)
            .data("__EVENTTARGET", __EVENTTARGET)
            .followRedirects(true)
            .post()
        val scheduleTable = result.select("div[class=mpei-tt-grid-wrap]")
        return SessionParser().parse(scheduleTable)
    }
}