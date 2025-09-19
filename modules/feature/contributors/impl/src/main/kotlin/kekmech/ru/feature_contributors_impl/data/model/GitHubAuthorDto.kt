package kekmech.ru.feature_contributors_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GitHubAuthorDto(
    @SerialName("login")
    val login: String,
)
