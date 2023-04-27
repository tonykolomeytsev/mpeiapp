package kekmech.ru.domain_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import retrofit2.Retrofit

object DomainScheduleModule : ModuleProvider({
    factory { get<Retrofit.Builder>().buildApi<ScheduleService>() }
    single { ScheduleRepository(get(), get(), get()) }
})
