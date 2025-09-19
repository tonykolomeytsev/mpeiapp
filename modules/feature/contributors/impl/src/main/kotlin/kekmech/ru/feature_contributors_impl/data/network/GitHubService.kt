package kekmech.ru.feature_contributors_impl.data.network

import kekmech.ru.feature_contributors_impl.data.model.GitHubContributorDto
import kekmech.ru.feature_contributors_impl.data.model.GitHubUserDto
import kekmech.ru.lib_network.BackendServiceUrl
import kekmech.ru.lib_network.EndpointUrl
import retrofit2.http.GET
import retrofit2.http.Path

@EndpointUrl(BackendServiceUrl.GITHUB)
internal interface GitHubService {

    @GET("repos/{user}/{repo}/stats/contributors")
    suspend fun getContributors(
        @Path("user") user: String = "tonykolomeytsev",
        @Path("repo") repository: String = "mpeiapp",
    ): List<GitHubContributorDto>

    @GET("users/{user}")
    suspend fun getUser(@Path("user") login: String): GitHubUserDto
}
