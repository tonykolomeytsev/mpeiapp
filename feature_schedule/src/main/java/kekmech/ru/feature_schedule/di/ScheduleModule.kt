package kekmech.ru.feature_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleService
import org.koin.dsl.bind
import retrofit2.Retrofit

object ScheduleModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ScheduleService>() } bind ScheduleService::class
})