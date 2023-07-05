package kekmech.ru.lib_analytics_android.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.lib_analytics_android.AnalyticsInitializer
import kekmech.ru.lib_analytics_android.AnalyticsWrapper
import kekmech.ru.lib_app_lifecycle.MainActivityLifecycleObserver
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val LibraryAnalyticsModule = module {
    factoryOf(::AnalyticsWrapper)
    factoryOf(::AnalyticsInitializer) bindIntoList MainActivityLifecycleObserver::class
}
