package kekmech.ru.feature_bars.di

import kekmech.ru.domain_bars.BarsFeatureLauncher
import kekmech.ru.feature_bars.launcher.BarsFeatureLauncherImpl
import kekmech.ru.feature_bars.screen.main.elm.BarsActor
import kekmech.ru.feature_bars.screen.main.elm.BarsStoreProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureBarsModule = module {
    factoryOf(::BarsActor)
    singleOf(::BarsStoreProvider)
    factoryOf(::BarsFeatureLauncherImpl) bind BarsFeatureLauncher::class
}
