package kekmech.ru.feature_map.di

import kekmech.ru.common_emoji.CommonEmojiModule
import kekmech.ru.common_network.retrofit.buildApi
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.domain_map.MapRepository
import kekmech.ru.domain_map.MapService
import kekmech.ru.feature_map.launcher.DeeplinkDelegate
import kekmech.ru.feature_map.launcher.MapFeatureLauncherImpl
import kekmech.ru.feature_map.screens.main.elm.MapActor
import kekmech.ru.feature_map.screens.main.elm.MapFeatureFactory
import kekmech.ru.feature_map.screens.main.view.MarkersBitmapFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureMapModule = module {
    includes(CommonEmojiModule)

    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    singleOf(::MapRepository) bind MapRepository::class
    singleOf(::DeeplinkDelegate) // TODO: refactor this
    factoryOf(::MapFeatureFactory)
    factoryOf(::MapActor)
    factoryOf(::MapDependencies)
    factory { MarkersBitmapFactory(androidContext(), get()) }
    factoryOf(::MapFeatureLauncherImpl) bind MapFeatureLauncher::class
}
