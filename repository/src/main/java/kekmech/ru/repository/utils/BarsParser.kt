package kekmech.ru.repository.utils

import kekmech.ru.core.dto.AcademicDiscipline
import kekmech.ru.core.dto.AcademicScore
import kekmech.ru.core.dto.ControlEvent
import kekmech.ru.core.dto.ControlWeek
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class BarsParser {

    private var currentAcademicDiscipline: AcademicDiscipline? = null
    private val academicDisciplines: MutableList<AcademicDiscipline> = mutableListOf()
    private var studentName: String = ""

    var isCurrentControlFlag = false

    fun parse(response: Document): AcademicScore {
        val contentDiv = response
            .select("div[id=div-StudyScore]")
        // внутри content div парсим каждый div
        contentDiv
            .first()
            .children()
            .forEach { pushDiscipline(it) }

        // добавляем лежащие в кэшэ последние данные
        if (currentAcademicDiscipline != null) {
            academicDisciplines.add(currentAcademicDiscipline!!)
            currentAcademicDiscipline = null
        }

        val infoDiv = response
            .select("div[id=div-FormHeader]")
            .first()
        pushStudentInfo(infoDiv)

        return AcademicScore(disciplines = academicDisciplines, studentName = studentName)
    }

    private fun pushStudentInfo(infoDiv: Element) {
        studentName = infoDiv
            .select("span")
            .html()
            .substringBefore('(')
            .trim()
    }

    private fun pushDiscipline(div: Element) {
        if (div.tagName() != "div") return
        val id = div.attr("id")
        if (id.isEmpty()) {
            if (currentAcademicDiscipline != null) {
                academicDisciplines.add(currentAcademicDiscipline!!)
                currentAcademicDiscipline = null
            }
            val name = div.select("strong").html().trim()
            if (name.isNotEmpty())
                currentAcademicDiscipline = AcademicDiscipline(name = name)
        }
        if (id.startsWith("s_ss")) {
            isCurrentControlFlag = false
            val rows = div.select("tr")
            rows.forEach { pushDisciplineRow(it) }
        }
    }

    private fun pushDisciplineRow(tr: Element) {
        println("=======================\n$tr")
        if (tr.tagName() != "tr") return
        val html = tr.html()
        if (html.contains("Текущий контроль")) {
            isCurrentControlFlag = true
            return
        } else if (html.contains("Балл текущего контроля")) {
            isCurrentControlFlag = false
        }

        if (isCurrentControlFlag) {
            val tds = tr.select("td")
            val eventName = tds[0].html().trim()
            val eventWeight = floatOrI(tds[1].html())
            val eventWeekNum = intOrI(tds[2].html())
            val eventMark = extractMark(tds[3].html())
            val event = ControlEvent(eventName, eventWeight, eventWeekNum, eventMark)
            currentAcademicDiscipline?.controlEvents?.add(event)

        } else if (html.contains("Контрольная неделя")) {
            val tds = tr.select("td")
            val controlWeekNum = intOrI(tds[1].html())
            val controlWeekMark = floatOrI(tds[2].html())
            val controlWeek = ControlWeek(controlWeekNum, controlWeekMark)
            currentAcademicDiscipline?.controlWeeks?.add(controlWeek)

        } else if (html.contains("Балл текущего контроля")) {
            val tds = tr.select("td")
            val currentControlMark = floatOrI(tds[1].html())
            currentAcademicDiscipline?.currentMark = currentControlMark

        } else if (html.contains("Промежуточная аттестация")) {
            val tds = tr.select("td")
            val examMark = floatOrI(tds[1].html())
            currentAcademicDiscipline?.examMark = examMark

        } else if (html.contains("Итоговая оценка")) {
            val tds = tr.select("td")
            val bothMarks = tds[1].html().split("<br>")
            val computedMark = extractMark(bothMarks[0].replace("[^0-9.,(]".toRegex(), ""))
            val finalMark = extractMark(bothMarks[1].replace("[^0-9.,(]".toRegex(), ""))
            currentAcademicDiscipline?.finalComputedMark = computedMark
            currentAcademicDiscipline?.finalFinalMark = finalMark
        }

    }

    private fun extractMark(string: String): Float {
        if (string.contains('(')) {
            return floatOrI(string.substringBefore('('))
        } else {
            return floatOrI(string)
        }
    }

    private fun floatOrI(string: String) = string.trim().replace(',', '.').toFloatOrNull() ?: -1f

    private fun intOrI(string: String) = string.trim().replace(',', '.').toIntOrNull() ?: -1
}