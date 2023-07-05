package kekmech.ru.feature_contributors_api.domain.model

import java.io.Serializable

data class Contributor(
    val login: String,
    val name: String?,
    val bio: String?,
    val url: String,
    val avatarUrl: String,
) : Serializable
