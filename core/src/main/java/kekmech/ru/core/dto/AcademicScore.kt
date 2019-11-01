package kekmech.ru.core.dto

data class AcademicScore(
    val studentName: String = "",
    val studentGroup: String = "",
    val studentScoreAcademic: Float = -1f,
    val studentScoreScience: Float = -1f,
    val studentScoreSocial: Float = -1f,
    val studentScoreSports: Float = -1f,
    val studentScoreSocial2: Float = -1f,
    val studentCourse: Int = -1,
    val studentQualification: String = "",
    val studentStudyingStatus: String = "",
    val disciplines: List<AcademicDiscipline>
)