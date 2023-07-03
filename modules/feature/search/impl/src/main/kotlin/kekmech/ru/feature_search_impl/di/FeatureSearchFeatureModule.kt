package kekmech.ru.feature_search_impl.di

import kekmech.ru.feature_search_api.SearchFeatureLauncher
import kekmech.ru.feature_search_impl.launcher.SearchFeatureLauncherImpl
import kekmech.ru.feature_search_impl.screens.main.elm.SearchActor
import kekmech.ru.feature_search_impl.screens.main.elm.SearchStoreFactory
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsActor
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsStoreFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureSearchFeatureModule = module{
    factoryOf(::SearchFeatureLauncherImpl) bind SearchFeatureLauncher::class
    factoryOf(::SearchDependencies)
    factoryOf(::SearchStoreFactory)
    factoryOf(::SearchActor)
    factoryOf(::ScheduleDetailsStoreFactory)
    factoryOf(::ScheduleDetailsActor)
}
