package kekmech.ru.domain_bars.dto

import java.io.Serializable

public class MarksResponse(
    public val payload: List<AssessedDiscipline>
)

public data class AssessedDiscipline(
    val name: String,
    val person: String,
    val assessmentType: String,
    val controlActivities: List<ControlActivity>,
    val finalGrades: List<FinalGrade>
) : Serializable

public data class ControlActivity(
    val name: String,
    val weight: String,
    val deadline: String,
    val finalMark: Float
) : Serializable

public data class FinalGrade(
    val name: String,
    val finalMark: Float,
    val type: FinalGradeType
) : Serializable

public enum class FinalGradeType : Serializable {
    CURRENT_SCORE,
    CONTROL_WEEK,
    INTERMEDIATE_MARK,
    FINAL_MARK
}
