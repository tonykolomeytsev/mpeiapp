package kekmech.ru.feature_bars_impl.data.dto

internal class RawMarksResponse(
    val payload: List<DisciplineRow>
)

internal class DisciplineRow(
    val disciplineName: String,
    val personName: String,
    val assessmentType: String,
    val units: String,
    val activities: List<ControlActivityRow>
)

internal class ControlActivityRow(
    val type: DisciplineRowType,
    val name: String? = null,
    val weight: String? = null,
    val weekNum: String? = null,
    val markAndDate: String? = null
)

@Suppress("unused")
internal enum class DisciplineRowType {
    UNDEFINED,
    CONTROL_ACTIVITY,
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}
