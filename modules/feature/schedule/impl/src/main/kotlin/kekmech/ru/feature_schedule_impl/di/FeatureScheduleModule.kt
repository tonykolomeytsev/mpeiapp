package kekmech.ru.feature_schedule_impl.di

import kekmech.ru.feature_schedule_api.PreheatSelectedScheduleProvider
import kekmech.ru.feature_schedule_api.ScheduleFeatureApi
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_api.domain.usecase.GetCurrentScheduleUseCase
import kekmech.ru.feature_schedule_api.domain.usecase.HasSelectedScheduleUseCase
import kekmech.ru.feature_schedule_impl.data.datasource.ScheduleCacheWrapper
import kekmech.ru.feature_schedule_impl.data.datasource.SelectedScheduleSource
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleRepositoryImpl
import kekmech.ru.feature_schedule_impl.data.repository.ScheduleSearchRepositoryImpl
import kekmech.ru.feature_schedule_impl.domain.usecase.HasSelectedScheduleUseCaseImpl
import kekmech.ru.feature_schedule_impl.launcher.ScheduleFeatureApiImpl
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.find_schedule.elm.FindScheduleStoreFactory
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleActor
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleStoreFactory
import kekmech.ru.lib_analytics_api.SelectedScheduleAnalyticsProvider
import kekmech.ru.lib_network.buildApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureScheduleModule = module {
    factoryOf(::ScheduleFeatureApiImpl) bind ScheduleFeatureApi::class
    factoryOf(::FindScheduleActor)

    factoryOf(::FindScheduleStoreFactory) bind FindScheduleStoreFactory::class
    singleOf(::ScheduleStoreFactory)
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
    factoryOf(::HasSelectedScheduleUseCaseImpl) bind HasSelectedScheduleUseCase::class
}
