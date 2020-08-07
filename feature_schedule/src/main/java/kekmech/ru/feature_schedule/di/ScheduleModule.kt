package kekmech.ru.feature_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import kekmech.ru.feature_schedule.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleActor
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleFeatureFactory
import org.koin.dsl.bind
import retrofit2.Retrofit

object ScheduleModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ScheduleService>() } bind ScheduleService::class
    single { ScheduleFeatureLauncherImpl(get()) } bind ScheduleFeatureLauncher::class
    single { ScheduleRepository(get()) } bind ScheduleRepository::class
    single { FindScheduleActor(get()) } bind FindScheduleActor::class

    factory { FindScheduleFeatureFactory(get()) } bind FindScheduleFeatureFactory::class
    factory { ScheduleDependencies(get()) } bind ScheduleDependencies::class
})