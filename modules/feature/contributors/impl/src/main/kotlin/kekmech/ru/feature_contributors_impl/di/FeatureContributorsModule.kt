package kekmech.ru.feature_contributors_impl.di

import kekmech.ru.feature_contributors_api.data.repository.ContributorsRepository
import kekmech.ru.feature_contributors_impl.data.network.GitHubService
import kekmech.ru.feature_contributors_impl.data.repository.ContributorsRepositoryImpl
import kekmech.ru.library_network.buildApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureContributorsModule = module {
    factory { get<Retrofit.Builder>().buildApi<GitHubService>() } bind GitHubService::class
    singleOf(::ContributorsRepositoryImpl) bind ContributorsRepository::class
}
