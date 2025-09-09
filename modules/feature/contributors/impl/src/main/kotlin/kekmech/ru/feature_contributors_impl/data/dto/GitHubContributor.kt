package kekmech.ru.feature_contributors_impl.data.dto

import com.google.errorprone.annotations.Keep

@Keep
internal data class GitHubContributor(
    val total: Long,
    val author: GitHubAuthor,
)
