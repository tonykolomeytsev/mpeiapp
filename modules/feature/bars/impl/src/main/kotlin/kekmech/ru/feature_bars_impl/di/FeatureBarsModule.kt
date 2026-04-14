package kekmech.ru.feature_bars_impl.di

import com.kekmech.perf_sdk.PerfSdk
import kekmech.ru.feature_bars_api.BarsFeatureLauncher
import kekmech.ru.feature_bars_impl.launcher.BarsFeatureLauncherImpl
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeActor
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureBarsModule = module {
    single { PerfSdk(dataDir = androidApplication().dataDir) }
    factoryOf(::BarsComposeActor)
    factoryOf(::BarsComposeStoreFactory)
    factoryOf(::BarsFeatureLauncherImpl) bind BarsFeatureLauncher::class
}
