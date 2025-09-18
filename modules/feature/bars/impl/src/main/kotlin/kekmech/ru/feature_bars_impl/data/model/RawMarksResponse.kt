package kekmech.ru.feature_bars_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RawMarksResponse(
    @SerialName("payload")
    val payload: List<DisciplineRow>,
)

@Serializable
internal class DisciplineRow(
    @SerialName("disciplineName")
    val disciplineName: String,

    @SerialName("personName")
    val personName: String,

    @SerialName("assessmentType")
    val assessmentType: String,

    @Suppress("Unused")
    @SerialName("units")
    val units: String,

    @SerialName("activities")
    val activities: List<ControlActivityRow>,
)

@Serializable
internal class ControlActivityRow(
    @SerialName("type")
    val type: DisciplineRowType,

    @SerialName("name")
    val name: String? = null,

    @SerialName("weight")
    val weight: String? = null,

    @SerialName("weekNum")
    val weekNum: String? = null,

    @SerialName("markAndDate")
    val markAndDate: String? = null,
)

@Serializable
internal enum class DisciplineRowType {
    @SerialName("UNDEFINED")
    UNDEFINED,

    @SerialName("CONTROL_ACTIVITY")
    CONTROL_ACTIVITY,

    @SerialName("CURRENT_SCORE")
    CURRENT_SCORE,

    @SerialName("CONTROL_WEEK")
    CONTROL_WEEK,

    @SerialName("INTERMEDIATE_MARK")
    INTERMEDIATE_MARK,

    @SerialName("FINAL_MARK")
    FINAL_MARK,
}
