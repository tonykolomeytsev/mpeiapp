package kekmech.ru.domain_schedule.di

import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_analytics.SelectedScheduleAnalyticsProvider
import kekmech.ru.domain_schedule.PreheatSelectedScheduleProvider
import kekmech.ru.domain_schedule.network.ScheduleService
import kekmech.ru.domain_schedule.repository.ScheduleRepository
import kekmech.ru.domain_schedule.repository.ScheduleRepositoryImpl
import kekmech.ru.domain_schedule.repository.ScheduleSearchRepository
import kekmech.ru.domain_schedule.use_cases.GetCurrentScheduleUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit

public val DomainScheduleModule: Module = module {
    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    singleOf(::ScheduleRepositoryImpl) binds arrayOf(
        ScheduleRepository::class,
        SelectedScheduleAnalyticsProvider::class,
        PreheatSelectedScheduleProvider::class,
    )
    factoryOf(::ScheduleSearchRepository)
    factoryOf(::GetCurrentScheduleUseCase)
}
