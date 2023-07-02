package kekmech.ru.common_analytics.di

import kekmech.ru.common_analytics.AnalyticsInitializer
import kekmech.ru.common_analytics.AnalyticsWrapper
import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.library_app_lifecycle.MainActivityLifecycleObserver
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val CommonAnalyticsModule = module {
    factoryOf(::AnalyticsWrapper)
    factoryOf(::AnalyticsInitializer) bindIntoList MainActivityLifecycleObserver::class
}
