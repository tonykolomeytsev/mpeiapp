package kekmech.ru.core.dto

import com.google.gson.annotations.SerializedName

data class ControlEvent(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("weight")
    val weight: Float = 0f,
    @SerializedName("w")
    val week: Int = 0,
    @SerializedName("m")
    val mark: Float = -1f
)