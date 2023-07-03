package kekmech.ru.feature_bars_impl.di

import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_bars_impl.data.datasource.BarsConfigDataSource
import kekmech.ru.feature_bars_impl.data.datasource.BarsExtractJsDataSource
import kekmech.ru.feature_bars_impl.data.network.BarsService
import kekmech.ru.feature_bars_impl.data.repository.BarsConfigRepository
import kekmech.ru.feature_bars_impl.data.repository.BarsExtractJsRepository
import kekmech.ru.feature_bars_impl.data.repository.BarsUserInfoRepository
import kekmech.ru.feature_bars_impl.launcher.BarsFeatureLauncherImpl
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsActor
import kekmech.ru.feature_bars_impl.presentation.screen.main.elm.BarsStoreProvider
import kekmech.ru.library_network.buildApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureBarsModule = module {
    factoryOf(::BarsActor)
    singleOf(::BarsStoreProvider)
    factoryOf(::BarsFeatureLauncherImpl) bind BarsFeatureLauncher::class

    factoryOf(::BarsConfigDataSource)
    factoryOf(::BarsConfigRepository)
    factoryOf(::BarsExtractJsDataSource)
    factoryOf(::BarsExtractJsRepository)
    factoryOf(::BarsUserInfoRepository)
    single { get<Retrofit.Builder>().buildApi<BarsService>() } bind BarsService::class
}
