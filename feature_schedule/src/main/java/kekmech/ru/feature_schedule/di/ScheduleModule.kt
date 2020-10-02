package kekmech.ru.feature_schedule.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.SchedulePersistentCache
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import kekmech.ru.feature_schedule.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule.find_schedule.FindScheduleAnalytics
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleActor
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleFeatureFactory
import kekmech.ru.feature_schedule.main.ScheduleAnalytics
import kekmech.ru.feature_schedule.main.presentation.ScheduleActor
import kekmech.ru.feature_schedule.main.presentation.ScheduleFeatureFactory
import org.koin.dsl.bind
import retrofit2.Retrofit

object ScheduleModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ScheduleService>() } bind ScheduleService::class
    single { ScheduleFeatureLauncherImpl(get()) } bind ScheduleFeatureLauncher::class
    single { ScheduleRepository(get(), get(), get()) } bind ScheduleRepository::class
    single { FindScheduleActor(get()) }

    factory { FindScheduleFeatureFactory(get()) } bind FindScheduleFeatureFactory::class
    factory { ScheduleFeatureFactory(get(), get()) } bind ScheduleFeatureFactory::class
    factory { ScheduleActor(get(), get()) }
    factory { ScheduleDependencies(get(), get()) }
    factory { ScheduleAnalytics(get()) } bind ScheduleAnalytics::class
    factory { FindScheduleAnalytics(get()) } bind FindScheduleAnalytics::class
    factory { SchedulePersistentCache(get(), get(), get()) } bind SchedulePersistentCache::class
})