package kekmech.ru.core.dto

import com.google.gson.annotations.SerializedName

data class AcademicScore(
    @SerializedName("name")
    val studentName: String = "",
    @SerializedName("group")
    val studentGroup: String = "",
    @SerializedName("r")
    val rating: Rating = Rating(),
    @SerializedName("course")
    val studentCourse: Int = -1,
    @SerializedName("q")
    val studentQualification: String = "",
    @SerializedName("stat")
    val studentStudyingStatus: String = "",
    @SerializedName("disc")
    val disciplines: List<AcademicDiscipline>
) {
    data class Rating(
        @SerializedName("t")
        var total: Int = 0,
        @SerializedName("e")
        var study: Int = 0,
        @SerializedName("s")
        var science: Int = 0,
        @SerializedName("st")
        var social_total: Int = 0,
        @SerializedName("ss")
        var social_sport: Int = 0,
        @SerializedName("sa")
        var social_act: Int = 0
    )
}