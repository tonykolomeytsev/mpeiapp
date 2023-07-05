package kekmech.ru.feature_contributors_impl.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

internal data class GitHubUser(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val gitHubPageUrl: String,
    val name: String?,
    val bio: String?,
) : Serializable
