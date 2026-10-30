package kekmech.ru.lib_bars_parser

private val WHITESPACE = Regex("\\s+")
private val LEADING_NUMBER = Regex("^(\\d+(?:[.,]\\d+)?)")
private val DATE_IN_BRACKETS = Regex("\\((\\d{2}\\.\\d{2}\\.\\d{2,4})\\)")
private val RETAKE = Regex("Пересдана:\\s*(\\S+)\\s*\\((\\d{2}\\.\\d{2}\\.\\d{2,4})\\)")
private val UNITS = Regex("^(\\d+(?:[.,]\\d+)?)\\s*ЗЕ(.*)$")

/** Collapses every kind of whitespace, non-breaking spaces included, into single spaces. */
fun normalizeBarsText(text: String): String =
    text.replace(' ', ' ').replace(WHITESPACE, " ").trim()

/**
 * Parses a mark cell. [mainText] is the text of the cell itself ("4  (06.06.26)"), [retakeText] is the line about
 * the retake ("Пересдана: 0 (11.04.26)") if the cell has one. Returns null for an empty cell.
 */
fun parseBarsMark(mainText: String, retakeText: String? = null): BarsMark? {
    val main = normalizeBarsText(mainText)
    val retakeLine = retakeText?.let(::normalizeBarsText).orEmpty()
    if (main.isEmpty() && retakeLine.isEmpty()) return null

    val numberMatch = LEADING_NUMBER.find(main)
    val value = numberMatch?.groupValues?.get(1)?.replace(',', '.')?.toFloatOrNull()
    val date = DATE_IN_BRACKETS.find(main)?.groupValues?.get(1)

    var rest = main
    if (numberMatch != null) rest = rest.removePrefix(numberMatch.value)
    rest = rest.replaceFirst(DATE_IN_BRACKETS, "")
    val note = normalizeBarsText(rest).ifEmpty { null }

    val retake = RETAKE.find(retakeLine)
    return BarsMark(
        raw = listOf(main, retakeLine).filter { it.isNotEmpty() }.joinToString(" "),
        value = value,
        date = date,
        retakenFrom = retake?.groupValues?.get(1),
        retakenDate = retake?.groupValues?.get(2),
        note = note,
    )
}

/** The first integer of a "15 (07.12.26-13.12.26)" cell, null if there is none. */
fun parseBarsWeekNumber(weekNum: String?): Int? {
    val number = weekNum?.let { LEADING_NUMBER.find(normalizeBarsText(it)) }?.groupValues?.get(1) ?: return null
    return number.substringBefore('.').substringBefore(',').toIntOrNull()
}

internal data class DisciplineHeader(
    val name: String,
    val person: String,
    val assessmentType: String,
    val units: String,
    val note: String?,
)

/**
 * "Дисциплина, Преподаватель, экзамен, 4ЗЕ [примечание]" -> parts. Only the discipline name may contain commas,
 * therefore the parts are taken from the end; commas inside brackets do not split.
 */
internal fun parseDisciplineHeader(text: String): DisciplineHeader {
    val parts = splitOutsideBrackets(normalizeBarsText(text)).toMutableList()

    var units = ""
    var note: String? = null
    val unitsMatch = parts.lastOrNull()?.let { UNITS.find(it) }
    if (unitsMatch != null) {
        units = unitsMatch.groupValues[1] + "ЗЕ"
        note = unitsMatch.groupValues[2].trim().ifEmpty { null }
        parts.removeAt(parts.lastIndex)
    }

    return when {
        parts.size >= 3 -> DisciplineHeader(
            name = parts.dropLast(2).joinToString(", "),
            person = parts[parts.size - 2],
            assessmentType = parts.last(),
            units = units,
            note = note,
        )
        parts.size == 2 -> DisciplineHeader(parts[0], person = "", assessmentType = parts[1], units = units, note = note)
        else -> DisciplineHeader(parts.firstOrNull().orEmpty(), person = "", assessmentType = "", units = units, note = note)
    }
}

private fun splitOutsideBrackets(text: String): List<String> {
    val parts = mutableListOf<String>()
    val current = StringBuilder()
    var depth = 0
    for (ch in text) {
        when {
            ch == '(' -> { depth++; current.append(ch) }
            ch == ')' -> { if (depth > 0) depth--; current.append(ch) }
            ch == ',' && depth == 0 -> { parts += current.toString().trim(); current.clear() }
            else -> current.append(ch)
        }
    }
    parts += current.toString().trim()
    return parts.filter { it.isNotEmpty() }
}
