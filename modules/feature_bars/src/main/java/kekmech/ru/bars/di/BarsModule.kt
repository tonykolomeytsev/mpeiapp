package kekmech.ru.bars.di

import kekmech.ru.bars.launcher.BarsFeatureLauncherImpl
import kekmech.ru.bars.screen.main.elm.BarsActor
import kekmech.ru.bars.screen.main.elm.BarsFeatureFactory
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.domain_bars.BarsRepository
import kekmech.ru.domain_bars.BarsService
import org.koin.dsl.bind
import retrofit2.Retrofit

object BarsModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<BarsService>() } bind BarsService::class
    factory { BarsRepository(get(), get()) }
    factory { BarsActor(get()) }
    factory { BarsFeatureFactory(get()) }
    factory { BarsFeatureLauncherImpl() } bind BarsFeatureLauncher::class
})