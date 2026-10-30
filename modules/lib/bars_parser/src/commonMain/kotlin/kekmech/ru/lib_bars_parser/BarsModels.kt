package kekmech.ru.lib_bars_parser

/** What a downloaded BARS page is. */
enum class BarsPageKind {
    /** "Оценки в БАРС": the marks of one semester of one student. */
    MARKS,

    /** "Сводка по оценкам БАРС и пропускам" (a whole page or the ajax part of it). */
    SUMMARY,

    /** The list of students of an account that has several of them. */
    STUDENT_LIST,

    /** The sign-in page: the session is absent or has expired. */
    LOGIN,

    /** The page that asks for the second factor (a one-time code) after the login and the password. */
    TWO_FACTOR,

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
    /** The rating block of the page, null if the page has none. */
    val rating: BarsRating? = null,
) {
    val selectedSemester: BarsSemester? get() = semesters.firstOrNull { it.selected }
}

/**
 * The rating block ("Учебная деятельность 12,0 / Учебный / Комплексный: 6 ..."). The set of labels depends on the
 * semester, so everything is kept as a list and the well-known ones have shortcuts.
 */
data class BarsRating(
    /** The semester the numbers belong to (data-sem-id of the total score). */
    val semesterId: String?,
    val items: List<BarsRatingItem>,
    /** The semesters of the rating filter. */
    val semesters: List<BarsSemester>,
) {
    fun value(label: String): Float? = items.firstOrNull { it.label == label }?.value

    val complex: Float? get() = value("Комплексный")
    val complexSemester: Float? get() = value("Комплексный за семестр")
    val study: Float? get() = value("Учебный")
    val studySemester: Float? get() = value("Учебный за семестр")
    val studyActivity: Float? get() = value("Учебная деятельность")
}

data class BarsRatingItem(
    /** "Комплексный за семестр", the colon is cut. */
    val label: String,
    /** 12.0 for "12,0", null if the cell is not a number. */
    val value: Float?,
    val raw: String,
)

/** The table of the page "Сводка": the marks of a semester by the weeks. */
data class BarsSummaryPage(
    /** "2025/2026, Весенний семестр" */
    val semesterName: String?,
    val semesters: List<BarsSemester>,
    /** The number of the current week, null if BARS does not mark it (a past semester). */
    val currentWeek: Int?,
    val rows: List<BarsSummaryRow>,
)

data class BarsSummaryRow(
    val discipline: String,
    /** "экзамен", the last brackets of the row title. */
    val assessmentType: String?,
    /** Only the weeks that have marks. */
    val weeks: List<BarsSummaryWeek>,
    /** "ТБ": the current score. */
    val currentScore: BarsMark?,
    /** "ПА": the intermediate attestation. */
    val intermediate: BarsMark?,
    /** "И": the final mark. */
    val finalMark: BarsMark?,
)

data class BarsSummaryWeek(
    val week: Int,
    val marks: List<BarsSummaryMark>,
)

data class BarsSummaryMark(
    /** The name of the control event (the tooltip of the mark), null if the page does not give it. */
    val title: String?,
    val mark: BarsMark,
)

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

/**
 * The sign-in form of "/BARS_Web". It is a plain html form that is posted to [action]; the page has no ajax in it.
 * [hiddenFields] has to be posted back as they are (the anti-forgery token among them), [accountField] and
 * [passwordField] are the names of the login and password inputs.
 */
data class BarsLoginForm(
    val action: String,
    val method: String,
    val accountField: String,
    val passwordField: String,
    /** The name of the "remember me" checkbox, null if the form has none. */
    val rememberMeField: String?,
    val hiddenFields: Map<String, String>,
) {
    val antiForgeryToken: String? get() = hiddenFields["__RequestVerificationToken"]
}

enum class BarsTwoFactorKind {
    /** One-time code of an authenticator app (provider 5, 6 digits). */
    TOTP,
    EMAIL,
    MAX,
    TELEGRAM,
    VK,
}

/** One way to get a code: a button `af2_code_send(buttonId, url, tid, length)` of the page. */
data class BarsTwoFactorProvider(
    val kind: BarsTwoFactorKind,
    val buttonId: String,
    /** The `tid` that goes to JSON_SendAF2_Code. */
    val tid: String,
    val codeLength: Int,
    /** The caption of the button ("Отправить повторно через МАКС"). */
    val caption: String,
)

data class BarsTwoFactorPage(
    /** The provider that the page starts with (the call in its script). */
    val default: BarsTwoFactorProvider,
    /** All the providers of the page, [default] included. */
    val providers: List<BarsTwoFactorProvider>,
    /** The address that sends a code, for example "/bars_web/Auth/JSON_SendAF2_Code". */
    val sendCodeUrl: String,
    /** The error that the page shows (a wrong code), null if there is none. */
    val error: String?,
    /** The form that the code is posted with, null if the page has no such form. */
    val form: BarsTwoFactorForm? = null,
)

/**
 * The form of the code entry: a post to [action] ("/bars_web/Auth/LoginCode") with the [hiddenFields] of the page
 * (the anti-forgery token and the login among them, they are on the page, not to be made up) and the code in [codeField].
 * The page itself asks BARS to send a code when it is loaded (`af2_code_send` for the default provider), a client
 * that does not run the page's scripts has to make that request itself.
 */
data class BarsTwoFactorForm(
    val action: String,
    val codeField: String,
    /** The maxlength of the code input, null if it has none. */
    val codeLength: Int?,
    val hiddenFields: Map<String, String>,
)

/**
 * The answer of JSON_SendAF2_Code: {"success":true,"message":"Сообщение отправлено в 'MAX'"},
 * with an "error" text instead of the message when BARS refuses.
 */
data class BarsSendCodeResult(
    val success: Boolean?,
    val message: String?,
    val error: String? = null,
)

/**
 * The marks of one semester as BARS sends them when the semester is changed (the inner part of the marks container:
 * `_PartialListStudent_SemesterSheet__Mark`). There is no header and no rating in it.
 */
data class BarsMarksPart(
    /** "2025/2026, Весенний семестр" */
    val semesterName: String?,
    val disciplines: List<BarsDiscipline>,
)
