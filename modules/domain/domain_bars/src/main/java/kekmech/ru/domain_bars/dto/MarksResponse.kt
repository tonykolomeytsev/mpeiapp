package kekmech.ru.domain_bars.dto

import java.io.Serializable

class MarksResponse(
    val payload: List<AssessedDiscipline>
)

data class AssessedDiscipline(
    val name: String,
    val person: String,
    val assessmentType: String,
    val controlActivities: List<ControlActivity>,
    val finalGrades: List<FinalGrade>
) : Serializable

data class ControlActivity(
    val name: String,
    val weight: String,
    val deadline: String,
    val finalMark: Float
) : Serializable

data class FinalGrade(
    val name: String,
    val finalMark: Float,
    val type: FinalGradeType
) : Serializable

enum class FinalGradeType : Serializable {
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}
