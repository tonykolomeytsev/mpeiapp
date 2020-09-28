package kekmech.ru.map.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_map.MapService
import kekmech.ru.map.MapAnalytics
import kekmech.ru.map.presentation.MapActor
import kekmech.ru.map.presentation.MapFeatureFactory
import org.koin.dsl.bind
import retrofit2.Retrofit

object MapModule : ModuleProvider({
    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    single { MapRepository(get(), get()) } bind MapRepository::class
    factory { MapFeatureFactory(get()) } bind MapFeatureFactory::class
    factory { MapActor(get()) } bind MapActor::class
    factory { MapDependencies(get()) } bind MapDependencies::class
    factory { MapAnalytics(get()) } bind MapAnalytics::class
})