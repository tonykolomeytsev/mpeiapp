package kekmech.ru.core.dto

import com.google.gson.annotations.SerializedName

data class AcademicScore(
    @SerializedName("name")
    val studentName: String = "",
    @SerializedName("group")
    val studentGroup: String = "",
    @SerializedName("s_ac")
    val studentScoreAcademic: Float = -1f,
    @SerializedName("s_sc")
    val studentScoreScience: Float = -1f,
    @SerializedName("s_so")
    val studentScoreSocial: Float = -1f,
    @SerializedName("s_sp")
    val studentScoreSports: Float = -1f,
    @SerializedName("s_so2")
    val studentScoreSocial2: Float = -1f,
    @SerializedName("course")
    val studentCourse: Int = -1,
    @SerializedName("q")
    val studentQualification: String = "",
    @SerializedName("stat")
    val studentStudyingStatus: String = "",
    @SerializedName("disc")
    val disciplines: List<AcademicDiscipline>
)