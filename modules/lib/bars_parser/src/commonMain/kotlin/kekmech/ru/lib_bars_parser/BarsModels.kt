package kekmech.ru.lib_bars_parser

/** What a downloaded BARS page is. */
enum class BarsPageKind {
    /** "Оценки в БАРС": the marks of one semester of one student. */
    MARKS,

    /** The list of students of an account that has several of them. */
    STUDENT_LIST,

    /** The sign-in page: the session is absent or has expired. */
    LOGIN,

    UNKNOWN,
}

/** The page cannot be parsed: BARS changed its markup or returned something else than the marks page. */
class BarsParseException(message: String) : Exception(message)

data class BarsMarksPage(
    /** "Фамилия Имя Отчество" without the "(№ЗК - ...)" part, null if the header is absent. */
    val studentName: String?,
    val studentGroup: String?,
    /** "Курс", "Направление", "Средний балл ПА", ... in the order of the page. */
    val meta: List<BarsMetaItem>,
    val semesters: List<BarsSemester>,
    val disciplines: List<BarsDiscipline>,
) {
    val selectedSemester: BarsSemester? get() = semesters.firstOrNull { it.selected }
}

data class BarsMetaItem(
    val description: String,
    val value: String,
)

data class BarsSemester(
    /** The value of the semester filter, the id that BARS accepts in the request. */
    val id: String,
    val name: String,
    val selected: Boolean,
)

data class BarsDiscipline(
    val name: String,
    /** The lecturer, "(руководитель - ...)" stays in. */
    val person: String,
    /** "экзамен", "зачёт с оценкой (по совокупности)", ... */
    val assessmentType: String,
    /** "4ЗЕ", empty if the page does not tell. */
    val units: String,
    /** The text after "сдать до", for example "07.06.2026". */
    val deadline: String?,
    /** Everything else that BARS writes next to the discipline: "частичная переаттестация", ... */
    val notes: List<String>,
    val activities: List<BarsActivity>,
)

enum class BarsActivityType {
    UNDEFINED,

    /** A control event (КМ) with a weight, a deadline and a mark. */
    CONTROL_ACTIVITY,

    /** "Балл текущего контроля". */
    CURRENT_SCORE,

    /** "Контрольная неделя №N". */
    CONTROL_WEEK,

    /** "Промежуточная аттестация ...". */
    INTERMEDIATE_MARK,

    /** "Итоговая оценка". */
    FINAL_MARK,
}

data class BarsActivity(
    val type: BarsActivityType,
    val name: String,
    /** The weight of a control event in percent, as written on the page. */
    val weight: String?,
    /** The second number column as written on the page: "15 (07.12.26-13.12.26)" for a control event. */
    val weekNum: String?,
    val mark: BarsMark?,
    /** The text of the grading criteria of a control event, if the page has it. */
    val criteria: String?,
)

/**
 * A mark cell: "4  (06.06.26)", "2,00", "2 ППА1 - 3 (не уч.)", with the retake line of the cell
 * ("Пересдана: 0 (11.04.26)"). Anything the parser does not understand stays in [raw].
 */
data class BarsMark(
    val raw: String,
    /** The leading number of the cell, null if there is none ("н/я", "зачтено"). */
    val value: Float?,
    /** "06.06.26" */
    val date: String?,
    /** The mark that was replaced by a retake, "0" for example. */
    val retakenFrom: String?,
    val retakenDate: String?,
    /** What follows the number except the date: "ППА1 - 3 (не уч.)". */
    val note: String?,
) {
    val isRetaken: Boolean get() = retakenFrom != null
}

data class BarsStudentLink(
    val id: String,
    val name: String,
)
