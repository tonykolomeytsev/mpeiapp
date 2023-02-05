package kekmech.ru.feature_map.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.common_emoji.EmojiModule
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_map.MapService
import kekmech.ru.feature_map.elm.MapActor
import kekmech.ru.feature_map.elm.MapFeatureFactory
import kekmech.ru.feature_map.launcher.DeeplinkDelegate
import kekmech.ru.feature_map.launcher.MapFeatureLauncherImpl
import kekmech.ru.feature_map.view.MarkersBitmapFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.dsl.bind
import retrofit2.Retrofit

object MapModule : ModuleProvider({
    loadKoinModules(EmojiModule.provider)

    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    single { MapRepository(get(), get()) } bind MapRepository::class
    single { DeeplinkDelegate() } // todo сделать это иначе
    factory { MapFeatureFactory(get(), get()) } bind MapFeatureFactory::class
    factory { MapActor(get()) } bind MapActor::class
    factory { MapDependencies(get(), get()) } bind MapDependencies::class
    factory { MarkersBitmapFactory(androidContext(), get()) } bind MarkersBitmapFactory::class
    factory { MapFeatureLauncherImpl(get()) } bind MapFeatureLauncher::class
})
