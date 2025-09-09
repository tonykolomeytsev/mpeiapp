package kekmech.ru.feature_contributors_impl.data.dto

import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
internal data class GitHubUser(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("html_url") val gitHubPageUrl: String,
    val name: String?,
    val bio: String?,
) : Serializable
