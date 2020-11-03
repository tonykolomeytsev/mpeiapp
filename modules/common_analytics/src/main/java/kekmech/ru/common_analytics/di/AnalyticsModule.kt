package kekmech.ru.common_analytics.di

import kekmech.ru.common_analytics.AnalyticsWrapper
import kekmech.ru.common_di.ModuleProvider

object AnalyticsModule : ModuleProvider({
    single { AnalyticsWrapper(get()) }
})