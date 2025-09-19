package kekmech.ru.feature_contributors_impl.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GitHubContributorDto(
    @SerialName("total")
    val total: Long,
    @SerialName("author")
    val author: GitHubAuthorDto,
)
