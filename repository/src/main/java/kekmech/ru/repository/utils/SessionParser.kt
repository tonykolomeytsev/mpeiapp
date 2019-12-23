package kekmech.ru.repository.utils

import android.util.Log
import kekmech.ru.core.dto.AcademicSession
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class SessionParser {
    private val academicEvents = mutableListOf<AcademicSession.Event>()
    private var academicEvent = AcademicSession.Event()

    fun parse(selection: Elements): AcademicSession {
        val rows = selection.select("tr").toMutableList()
        rows.removeAt(0) // remove table header
        rows.forEach { row -> try {
            pushRow(row)
        } catch (e: Exception) { /* do nothing */ } }

        return AcademicSession(academicEvents)
    }

    private fun pushRow(row: Element) {
        academicEvent = AcademicSession.Event()
        val tds = row.select("td")
        parseDateTime(tds[0])
        parseDisciplineName(tds[1])
        parseTeacherName(tds[2])
        parsePlace(tds[3])

        academicEvents += academicEvent
    }

    private fun parsePlace(td: Element) {
        try {
            academicEvent.place = td.html()
        } catch (e: Exception) {
            Log.d("SessionParser", "Не удалось распарсить номер аудитории")
        }
    }

    private fun parseTeacherName(td: Element) {
        try {
            academicEvent.teacher = td.select("a").html()
        } catch (e: Exception) {
            Log.d("SessionParser","Не удалось распарсить имя преподавателя ${td.html()}")
        }
    }

    private fun parseDisciplineName(td: Element) {
        try {
            academicEvent.name = td.html().replace("<br>", " ")
        } catch (e: Exception) {
            Log.d("SessionParser","Не удалось распарсить название экзамена ${td.html()}")
        }
    }

    private fun parseDateTime(td: Element) {
        val html = td.html().replace('\n', ' ')
        val date = "(\\d{2}\\.\\d{2}\\.\\d{4})".toRegex().find(html)?.groups?.get(1)?.value ?: ""
        val time = "(\\d+:\\d+)".toRegex().find(html)?.groups?.get(1)?.value ?: ""
        academicEvent.startDate = date
        academicEvent.startTime = time
    }
}