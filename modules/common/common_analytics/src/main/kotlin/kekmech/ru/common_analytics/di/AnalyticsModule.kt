package kekmech.ru.common_analytics.di

import kekmech.ru.common_analytics.AnalyticsWrapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val CommonAnalyticsModule = module {
    singleOf(::AnalyticsWrapper)
}
