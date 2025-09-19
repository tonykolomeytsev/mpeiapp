package kekmech.ru.feature_contributors_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GitHubUserDto(
    @SerialName("id")
    val id: Long,

    @SerialName("login")
    val login: String,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("html_url")
    val gitHubPageUrl: String,

    @SerialName("name")
    val name: String?,

    @SerialName("bio")
    val bio: String?,
)
