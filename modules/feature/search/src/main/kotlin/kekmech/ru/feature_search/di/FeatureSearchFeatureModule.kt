package kekmech.ru.feature_search.di

import kekmech.ru.domain_search.SearchFeatureLauncher
import kekmech.ru.feature_search.launcher.SearchFeatureLauncherImpl
import kekmech.ru.feature_search.screens.main.elm.SearchActor
import kekmech.ru.feature_search.screens.main.elm.SearchFeatureFactory
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsActor
import kekmech.ru.feature_search.screens.schedule_details.elm.ScheduleDetailsFeatureFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureSearchFeatureModule = module{
    factoryOf(::SearchFeatureLauncherImpl) bind SearchFeatureLauncher::class
    factoryOf(::SearchDependencies)
    factoryOf(::SearchFeatureFactory)
    factoryOf(::SearchActor)
    factoryOf(::ScheduleDetailsFeatureFactory)
    factoryOf(::ScheduleDetailsActor)
}
