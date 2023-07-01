package kekmech.ru.common_analytics.di

import kekmech.ru.common_analytics.AnalyticsInitializer
import kekmech.ru.common_analytics.AnalyticsWrapper
import kekmech.ru.common_app_lifecycle.MainActivityLifecycleObserver
import kekmech.ru.common_di.bindIntoList
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val CommonAnalyticsModule = module {
    factoryOf(::AnalyticsWrapper)
    factoryOf(::AnalyticsInitializer) bindIntoList MainActivityLifecycleObserver::class
}
