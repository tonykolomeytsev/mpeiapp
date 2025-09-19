package kekmech.ru.feature_bars_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteBarsConfigDto(
    @SerialName("loginUrl")
    val loginUrl: String,

    @SerialName("studentListUrl")
    val studentListUrl: String,

    @SerialName("marksListUrl")
    val marksListUrl: String,

    @SerialName("logoutUrl")
    val logoutUrl: String,
)
