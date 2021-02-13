package kekmech.ru.feature_search.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_search.main.SearchAnalytics
import kekmech.ru.feature_search.launcher.SearchFeatureLauncherImpl
import kekmech.ru.feature_search.main.mvi.SearchActor
import kekmech.ru.feature_search.main.mvi.SearchFeatureFactory
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsActor
import kekmech.ru.feature_search.schedule_details.mvi.ScheduleDetailsFeatureFactory
import org.koin.dsl.bind

object SearchFeatureModule : ModuleProvider({
    factory { SearchAnalytics(get()) }
    factory { SearchFeatureLauncherImpl(get()) } bind SearchFeatureLauncher::class
    factory { SearchDependencies(get(), get(), get()) }
    factory { SearchFeatureFactory(get()) }
    factory { SearchActor(get(), get(), get()) }
    factory { ScheduleDetailsFeatureFactory(get()) }
    factory { ScheduleDetailsActor(get()) }
})