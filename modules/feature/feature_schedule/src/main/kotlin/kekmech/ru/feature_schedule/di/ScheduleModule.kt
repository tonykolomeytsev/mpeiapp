package kekmech.ru.feature_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import kekmech.ru.feature_schedule.launcher.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleActor
import kekmech.ru.feature_schedule.screens.find_schedule.elm.FindScheduleFeatureFactory
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleActor
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleFeatureFactory
import org.koin.dsl.bind
import retrofit2.Retrofit

object ScheduleModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ScheduleService>() } bind ScheduleService::class
    single { ScheduleFeatureLauncherImpl(get()) } bind ScheduleFeatureLauncher::class
    single { ScheduleRepository(get(), get(), get()) }
    single { FindScheduleActor(get()) }

    factory { FindScheduleFeatureFactory(get()) } bind FindScheduleFeatureFactory::class
    factory { ScheduleFeatureFactory(get(), get()) } bind ScheduleFeatureFactory::class
    factory { ScheduleActor(get(), get()) }
    factory { ScheduleDependencies(get(), get(), get()) }
})
