package kekmech.ru.feature_bars.di

import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_bars.BarsRepository
import kekmech.ru.domain_bars.BarsService
import kekmech.ru.feature_bars.launcher.BarsFeatureLauncherImpl
import kekmech.ru.feature_bars.screen.main.elm.BarsActor
import kekmech.ru.feature_bars.screen.main.elm.BarsFeatureFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureBarsModule = module {
    single { get<Retrofit.Builder>().buildApi<BarsService>() } bind BarsService::class
    factoryOf(::BarsRepository)
    factoryOf(::BarsActor)
    factoryOf(::BarsFeatureFactory)
    factoryOf(::BarsFeatureLauncherImpl) bind BarsFeatureLauncher::class
}
