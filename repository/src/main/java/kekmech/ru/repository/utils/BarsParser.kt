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
    private var studentName: String = "Не удалось загрузить имя"
    private var studentGroup: String = "Ошибка"
    private var studentQualification: String = ""
    private var studentSemester: String = ""
    private var rating = AcademicScore.Rating()

    var isCurrentControlFlag = false

    fun parse(response: Document): AcademicScore {
        val contentDiv = response
            .select("div[id=div-StudyScore]")
            .firstOrNull()
            ?: throw LoginException()
        // внутри content div парсим каждый div
        contentDiv
            .children()
            .forEach { pushDiscipline(it) }

        // добавляем лежащие в кэшэ последние данные
        if (currentAcademicDiscipline != null) {
            academicDisciplines.add(currentAcademicDiscipline!!)
            currentAcademicDiscipline = null
        }

        val infoDiv = response
            .select("div[id=div-FormHeader]")
            .firstOrNull()
        try {
            if (infoDiv != null)
                pushStudentInfo(infoDiv)
        } catch (e: Exception) {
            // Crashlytics.log(1, "BarsParser", "pushStudentInfo ERROR $e \n ${infoDiv?.html()}")
        }
        try {
            if (infoDiv != null)
                pushRatingInfo(infoDiv)
        } catch (e: Exception) {
            // Crashlytics.log(1, "BarsParser", "pushRatingInfo ERROR $e \n ${infoDiv?.html()}")
        }

        return AcademicScore(
            disciplines = academicDisciplines,
            studentName = studentName,
            studentGroup = studentGroup,
            studentQualification = studentQualification,
            studentSemester = studentSemester,
            rating = rating)
    }

    private fun pushRatingInfo(infoDiv: Element) {
        rating.total = infoDiv.select("span[id=total_score]").html().trim().toIntOrNull() ?: 0
        rating.study = infoDiv.select("span[id=study_score]").html().trim().toIntOrNull() ?: 0
        rating.science = infoDiv.select("span[id=science_score]").html().trim().toIntOrNull() ?: 0
        rating.social_total = infoDiv.select("span[id=social_score]").html().trim().toIntOrNull() ?: 0
        rating.social_sport = infoDiv.select("span[id=sport_score]").html().trim().toIntOrNull() ?: 0
        rating.social_act = infoDiv.select("span[id=social_activity_score]").html().trim().toIntOrNull() ?: 0
    }

    private fun pushStudentInfo(infoDiv: Element) {
        val span = infoDiv
            .select("span")
            ?.html() ?: ""
        if (span.isNotEmpty()) {
            studentName = span
                .substringBefore('(')
                .trim()
            studentGroup = span
                .substringAfter(')')
                .substringBefore('\n')
                .trim()
        }
        val q = infoDiv
            .select("li")
            ?.find { li -> li.html().contains("Ква") }
            ?.select("strong")
            ?.html() ?: ""
        if (q.isNotEmpty()) studentQualification = q.trim()

        val s = infoDiv
            .select("option")
            .firstOrNull()
            ?.html()
            ?.let { when {
                it.contains("Осе") -> "Осенний"
                it.contains("Весе") -> "Весенний"
                else -> ""
            } } ?: ""
        if (s.isNotEmpty()) studentSemester = s
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
            rows.forEach { try { pushDisciplineRow(it) } catch (e: Exception) {
                // Crashlytics.log(1, "BarsParser", "pushDisciplineRow ERROR $e \n ${div.html()}")
            } }
        }
    }

    private fun pushDisciplineRow(tr: Element) {
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
            try {
                val examType = "\\((.+)\\)".toRegex().find(html)?.groupValues?.getOrNull(1) ?: ""
                if (examType.length < 20) currentAcademicDiscipline?.examType = examType
            } catch (e: Exception) { e.printStackTrace() }

        } else if (html.contains("Итоговая оценка")) {
            val tds = tr.select("td")
            val bothMarks = tds[1].html().split("<br>")
//            val computedMark = extractMark(bothMarks[0].replace("[^0-9.,(]".toRegex(), ""))
            val finalMark = extractMark(bothMarks[0].replace("[^0-9.,(]".toRegex(), ""))
//            currentAcademicDiscipline?.finalComputedMark = computedMark
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

    class LoginException : RuntimeException("Не удалось залогиниться, неверные данные для входа")
}