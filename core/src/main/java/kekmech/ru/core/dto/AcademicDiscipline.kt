package kekmech.ru.core.dto

import com.google.gson.annotations.SerializedName

data class AcademicDiscipline(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("weeks")
    var controlWeeks: MutableList<ControlWeek> = mutableListOf(),
    @SerializedName("events")
    var controlEvents: MutableList<ControlEvent> = mutableListOf(),
    @SerializedName("c_mark")
    var currentMark: Float = -1f,
    @SerializedName("e_mark")
    var examMark: Float = -1f,
    @SerializedName("f_c_mark")
    var finalComputedMark: Float = -1f,
    @SerializedName("f_f_mark")
    var finalFinalMark: Float = -1f
)