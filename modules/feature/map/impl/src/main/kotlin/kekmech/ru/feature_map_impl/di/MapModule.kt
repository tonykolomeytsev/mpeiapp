package kekmech.ru.feature_map_impl.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.feature_map_api.data.repository.MapRepository
import kekmech.ru.feature_map_api.presentation.navigation.MapScreenApi
import kekmech.ru.feature_map_impl.MapInitializer
import kekmech.ru.feature_map_impl.data.network.MapService
import kekmech.ru.feature_map_impl.data.repository.MapRepositoryImpl
import kekmech.ru.feature_map_impl.launcher.DeeplinkDelegate
import kekmech.ru.feature_map_impl.launcher.MapFeatureLauncherImpl
import kekmech.ru.feature_map_impl.presentation.navigation.MapScreenApiImpl
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapActor
import kekmech.ru.feature_map_impl.presentation.screen.main.elm.MapStoreProvider
import kekmech.ru.feature_map_impl.presentation.screen.main.view.MarkersBitmapFactory
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.lib_emoji.CommonEmojiModule
import kekmech.ru.lib_network.buildApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

val FeatureMapModule = module {
    includes(CommonEmojiModule)

    singleOf(::DeeplinkDelegate) // TODO: refactor this
    singleOf(::MapStoreProvider)
    factoryOf(::MapActor)
    factoryOf(::MapDependencies)
    factory { MarkersBitmapFactory(androidContext(), get()) }
    factoryOf(::MapFeatureLauncherImpl) bind MapFeatureLauncher::class
    factoryOf(::MapInitializer) bindIntoList MainActivityLifecycleObserver::class

    single { get<Retrofit.Builder>().buildApi<MapService>() } bind MapService::class
    factoryOf(::MapRepositoryImpl) bind MapRepository::class
    factoryOf(::MapScreenApiImpl) bind MapScreenApi::class
}
