package kekmech.ru.map.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_emoji.EmojiModule
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_map.MapService
import kekmech.ru.map.MapAnalytics
import kekmech.ru.map.presentation.MapActor
import kekmech.ru.map.presentation.MapFeatureFactory
import kekmech.ru.map.view.MarkersBitmapFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import retrofit2.Retrofit

object MapModule : ModuleProvider({
    loadKoinModules(EmojiModule.provider)

    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    single { MapRepository(get(), get()) } bind MapRepository::class
    factory { MapFeatureFactory(get()) } bind MapFeatureFactory::class
    factory { MapActor(get()) } bind MapActor::class
    factory { MapDependencies(get()) } bind MapDependencies::class
    factory { MapAnalytics(get()) } bind MapAnalytics::class
    factory { MarkersBitmapFactory(androidContext(), get()) } bind MarkersBitmapFactory::class
})