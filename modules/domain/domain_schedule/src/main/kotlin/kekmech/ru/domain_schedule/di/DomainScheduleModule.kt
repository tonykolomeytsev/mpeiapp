package kekmech.ru.domain_schedule.di

import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

val DomainScheduleModule = module {
    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    singleOf(::ScheduleRepository)
}
