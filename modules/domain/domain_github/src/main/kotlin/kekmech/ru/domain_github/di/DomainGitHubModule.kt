package kekmech.ru.domain_github.di

import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_github.ContributorsRepository
import kekmech.ru.domain_github.GitHubService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

public val DomainGitHubModule: Module = module {
    factory { get<Retrofit.Builder>().buildApi<GitHubService>() } bind GitHubService::class
    singleOf(::ContributorsRepository)
}
