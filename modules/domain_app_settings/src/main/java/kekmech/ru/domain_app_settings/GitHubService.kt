package kekmech.ru.domain_app_settings

import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_annotations.BackendServiceUrl
import kekmech.ru.common_annotations.EndpointUrl
import kekmech.ru.domain_app_settings.dto.ContributorsItem
import kekmech.ru.domain_app_settings.dto.GitHubUser
import retrofit2.http.GET
import retrofit2.http.Path

@EndpointUrl(BackendServiceUrl.GITHUB)
interface GitHubService {

    @GET("repos/{user}/{repo}/stats/contributors")
    fun getContributors(
        @Path("user") user: String = "tonykolomeytsev",
        @Path("repo") repository: String = "mpeiapp",
    ): Single<List<ContributorsItem>>

    @GET("users/{user}")
    fun getUser(@Path("user") login: String): Single<GitHubUser>
}