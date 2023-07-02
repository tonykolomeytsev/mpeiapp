package kekmech.ru.domain_github.di

import kekmech.ru.domain_github.ContributorsRepository
import kekmech.ru.domain_github.GitHubService
import kekmech.ru.library_network.buildApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val DomainGitHubModule = module {
    factory { get<Retrofit.Builder>().buildApi<GitHubService>() } bind GitHubService::class
    singleOf(::ContributorsRepository)
}
