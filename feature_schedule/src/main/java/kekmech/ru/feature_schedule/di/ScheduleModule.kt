package kekmech.ru.feature_schedule.di

import android.content.Context.MODE_PRIVATE
import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_schedule.ScheduleFeatureLauncher
import kekmech.ru.domain_schedule.ScheduleRepository
import kekmech.ru.domain_schedule.ScheduleService
import kekmech.ru.feature_schedule.ScheduleFeatureLauncherImpl
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleActor
import kekmech.ru.feature_schedule.find_schedule.presentation.FindScheduleFeatureFactory
import kekmech.ru.feature_schedule.main.presentation.ScheduleFeatureFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import retrofit2.Retrofit

object ScheduleModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<ScheduleService>() } bind ScheduleService::class
    single { ScheduleFeatureLauncherImpl(get()) } bind ScheduleFeatureLauncher::class
    single { ScheduleRepository(get(), androidApplication().getSharedPreferences("Schedule", MODE_PRIVATE)) } bind ScheduleRepository::class
    single { FindScheduleActor(get()) } bind FindScheduleActor::class

    factory { FindScheduleFeatureFactory(get()) } bind FindScheduleFeatureFactory::class
    factory { ScheduleFeatureFactory(get(), get()) } bind ScheduleFeatureFactory::class
    factory { ScheduleDependencies(get()) } bind ScheduleDependencies::class
})