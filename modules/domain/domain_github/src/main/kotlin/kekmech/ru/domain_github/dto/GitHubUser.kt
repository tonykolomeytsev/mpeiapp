package kekmech.ru.domain_github.dto

import com.google.gson.annotations.SerializedName

data class GitHubUser(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val gitHubPageUrl: String,
    val name: String?,
    val bio: String?,
)
