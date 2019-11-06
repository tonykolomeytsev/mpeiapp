package kekmech.ru.core.dto

import com.google.gson.annotations.SerializedName

data class ControlWeek(
    @SerializedName("w")
    val week: Int = 0,
    @SerializedName("m")
    val mark: Float = -1f
)