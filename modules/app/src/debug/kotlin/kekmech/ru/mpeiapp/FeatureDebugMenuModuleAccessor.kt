package kekmech.ru.mpeiapp

import kekmech.ru.feature_debug_menu.di.FeatureDebugMenuModule
import org.koin.core.KoinApplication

fun KoinApplication.includeFeatureDebugMenuModule() {
    modules(FeatureDebugMenuModule)
}
