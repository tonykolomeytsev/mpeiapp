package kekmech.ru.domain_bars.dto

class RawMarksResponse(
    val payload: List<DisciplineRow>
)

class DisciplineRow(
    val disciplineName: String,
    val personName: String,
    val assessmentType: String,
    val units: String,
    val activities: List<ControlActivityRow>
)

class ControlActivityRow(
    val type: DisciplineRowType,
    val name: String? = null,
    val weight: String? = null,
    val weekNum: String? = null,
    val markAndDate: String? = null
)

enum class DisciplineRowType {
    UNDEFINED,
    CONTROL_ACTIVITY,
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}