package kekmech.ru.debug_menu.di

import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.debug_menu.launcher.DebugMenuInitializer
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureDebugMenuModule = module {
    factory { DebugMenuInitializer() } bind MainActivityLifecycleObserver::class
}
