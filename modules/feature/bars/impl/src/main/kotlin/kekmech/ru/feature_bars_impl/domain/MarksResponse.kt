package kekmech.ru.feature_bars_impl.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class MarksResponse(
    val payload: List<AssessedDiscipline>
)

@Parcelize
data class AssessedDiscipline(
    val name: String,
    val person: String,
    val assessmentType: String,
    val controlActivities: List<ControlActivity>,
    val finalGrades: List<FinalGrade>
) : Parcelable, Serializable

@Parcelize
data class ControlActivity(
    val name: String,
    val weight: String,
    val deadline: String,
    val finalMark: Float
) : Parcelable, Serializable

@Parcelize
data class FinalGrade(
    val name: String,
    val finalMark: Float,
    val type: FinalGradeType
) : Parcelable, Serializable

@Parcelize
enum class FinalGradeType : Parcelable, Serializable {
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}
