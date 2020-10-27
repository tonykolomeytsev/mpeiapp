package kekmech.ru.feature_search.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_search.launcher.SearchFeatureLauncherImpl
import kekmech.ru.feature_search.mvi.SearchActor
import kekmech.ru.feature_search.mvi.SearchFeatureFactory
import org.koin.dsl.bind

object SearchFeatureModule : ModuleProvider({
    factory { SearchFeatureLauncherImpl(get()) } bind SearchFeatureLauncher::class
    factory { SearchDependencies(get()) }
    factory { SearchFeatureFactory(get()) }
    factory { SearchActor(get(), get()) }
})