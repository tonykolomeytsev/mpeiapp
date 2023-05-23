package kekmech.ru.feature_schedule.di

import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule.launcher.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleActor
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleFeatureFactory
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleActor
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleStoreProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureScheduleModule = module {
    factoryOf(::ScheduleFeatureLauncherImpl) bind ScheduleFeatureLauncher::class
    factoryOf(::FindScheduleActor)

    factoryOf(::FindScheduleFeatureFactory) bind FindScheduleFeatureFactory::class
    singleOf(::ScheduleStoreProvider)
    factoryOf(::ScheduleActor)
    factoryOf(::ScheduleDependencies)
}
