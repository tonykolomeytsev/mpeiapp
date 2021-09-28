package kekmech.ru.domain_bars.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
) : Parcelable

@Parcelize
data class ControlActivity(
    val name: String,
    val weight: String,
    val deadline: String,
    val finalMark: Float
) : Parcelable

@Parcelize
data class FinalGrade(
    val name: String,
    val finalMark: Float,
    val type: FinalGradeType
) : Parcelable

@Parcelize
enum class FinalGradeType : Parcelable {
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}