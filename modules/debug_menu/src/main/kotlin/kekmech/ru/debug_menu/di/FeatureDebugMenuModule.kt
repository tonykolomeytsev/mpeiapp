package kekmech.ru.debug_menu.di

import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.common_di.bindIntoList
import kekmech.ru.debug_menu.initializer.DebugMenuInitializer
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuActor
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val DebugMenuModule = module {
    factory { DebugMenuInitializer() } bindIntoList MainActivityLifecycleObserver::class
    factoryOf(::DebugMenuStoreFactory)
    factoryOf(::DebugMenuActor)
}
