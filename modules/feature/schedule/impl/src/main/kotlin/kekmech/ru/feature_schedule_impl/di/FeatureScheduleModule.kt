package kekmech.ru.feature_schedule_impl.di

import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import kekmech.ru.feature_schedule_api.ScheduleFeatureLauncher
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_api.presentation.navigation.ScheduleScreenApi
import kekmech.ru.feature_schedule_api.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.feature_schedule_impl.data.datasource.ScheduleCacheWrapper
import kekmech.ru.feature_schedule_impl.data.datasource.SelectedScheduleSource
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleRepositoryImpl
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleSearchRepositoryImpl
import kekmech.ru.feature_schedule_impl.launcher.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule_impl.presentation.navigation.ScheduleScreenApiImpl
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreProvider
import kekmech.ru.lib_analytics_api.SelectedScheduleAnalyticsProvider
import kekmech.ru.lib_network.buildApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureScheduleModule = module {
    factoryOf(::ScheduleFeatureLauncherImpl) bind ScheduleFeatureLauncher::class
    factoryOf(::FindScheduleActor)

    factoryOf(::FindScheduleStoreFactory) bind FindScheduleStoreFactory::class
    singleOf(::ScheduleStoreProvider)
    factoryOf(::ScheduleActor)
    factoryOf(::ScheduleDependencies)

    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    singleOf(::ScheduleRepositoryImpl) binds arrayOf(
        ScheduleRepository::class,
        SelectedScheduleAnalyticsProvider::class,
        PreheatSelectedScheduleProvider::class,
    )
    factoryOf(::ScheduleSearchRepositoryImpl) bind ScheduleSearchRepository::class
    factoryOf(::GetCurrentScheduleUseCase)
    factoryOf(::SelectedScheduleSource)
    factoryOf(::ScheduleCacheWrapper)
    factoryOf(::ScheduleScreenApiImpl) bind ScheduleScreenApi::class
}
