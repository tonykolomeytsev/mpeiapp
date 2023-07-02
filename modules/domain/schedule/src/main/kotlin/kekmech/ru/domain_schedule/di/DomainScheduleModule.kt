package kekmech.ru.domain_schedule.di

import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import kekmech.ru.domain_schedule.data.ScheduleCacheWrapper
import kekmech.ru.domain_schedule.data.ScheduleRepository
import kekmech.ru.domain_schedule.data.ScheduleRepositoryImpl
import kekmech.ru.domain_schedule.data.ScheduleSearchRepository
import kekmech.ru.domain_schedule.data.SelectedScheduleSource
import kekmech.ru.domain_schedule.network.ScheduleService
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import kekmech.ru.library_analytics_api.SelectedScheduleAnalyticsProvider
import kekmech.ru.library_network.buildApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

val DomainScheduleModule = module {
    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    singleOf(::ScheduleRepositoryImpl) binds arrayOf(
        ScheduleRepository::class,
        SelectedScheduleAnalyticsProvider::class,
        PreheatSelectedScheduleProvider::class,
    )
    factoryOf(::ScheduleSearchRepository)
    factoryOf(::GetCurrentScheduleUseCase)
    factoryOf(::SelectedScheduleSource)
    factoryOf(::ScheduleCacheWrapper)
}
