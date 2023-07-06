package kekmech.ru.debug_menu.di

import kekmech.ru.debug_menu.DebugMenuInitializer
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.FeatureTogglesOverwriteMiddleware
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesActor
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesStoreFactory
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuActor
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kekmech.ru.lib_feature_toggles.RemoteVariableValueHolder
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DebugMenuModule = module {
    factory { DebugMenuInitializer() } bindIntoList AppLifecycleObserver::class
    factoryOf(::DebugMenuStoreFactory)
    factoryOf(::DebugMenuActor)

    factoryOf(::FeatureTogglesStoreFactory)
    factory { FeatureTogglesActor(getAll(), get()) }
    singleOf(::FeatureTogglesOverwriteMiddleware) bind RemoteVariableValueHolder.Middleware::class
}
