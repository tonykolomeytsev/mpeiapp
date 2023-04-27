package kekmech.ru.domain_github.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_github.ContributorsRepository
import kekmech.ru.domain_github.GitHubService
import org.koin.dsl.bind
import retrofit2.Retrofit

object DomainGitHubModule : ModuleProvider({
    factory { get<Retrofit.Builder>().buildApi<GitHubService>() } bind GitHubService::class
    single { ContributorsRepository(get(), get()) }
})
