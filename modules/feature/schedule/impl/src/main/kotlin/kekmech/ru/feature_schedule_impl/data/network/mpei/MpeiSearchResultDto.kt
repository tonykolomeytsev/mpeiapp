package kekmech.ru.feature_schedule_impl.data.network.mpei

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MpeiSearchResultDto(
    @SerialName("id")
    val id: Long,
    @SerialName("label")
    val label: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("type")
    val type: String,
)
