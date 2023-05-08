package kekmech.ru.common_analytics.di

import kekmech.ru.common_analytics.AnalyticsWrapper
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val CommonAnalyticsModule = module {
    factoryOf(::AnalyticsWrapper)
}
