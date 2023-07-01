package kekmech.ru.feature_map.di

import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.common_di.bindIntoList
import kekmech.ru.common_emoji.CommonEmojiModule
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.feature_map.MapInitializer
import kekmech.ru.feature_map.launcher.DeeplinkDelegate
import kekmech.ru.feature_map.launcher.MapFeatureLauncherImpl
import kekmech.ru.feature_map.screens.main.elm.MapActor
import kekmech.ru.feature_map.screens.main.elm.MapStoreProvider
import kekmech.ru.feature_map.screens.main.view.MarkersBitmapFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureMapModule = module {
    includes(CommonEmojiModule)

    singleOf(::DeeplinkDelegate) // TODO: refactor this
    singleOf(::MapStoreProvider)
    factoryOf(::MapActor)
    factoryOf(::MapDependencies)
    factory { MarkersBitmapFactory(androidContext(), get()) }
    factoryOf(::MapFeatureLauncherImpl) bind MapFeatureLauncher::class
    factoryOf(::MapInitializer) bindIntoList MainActivityLifecycleObserver::class
}
