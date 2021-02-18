package kekmech.ru.feature_dashboard.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.feature_dashboard.DashboardAnalytics
import kekmech.ru.feature_dashboard.elm.DashboardActor
import kekmech.ru.feature_dashboard.elm.DashboardFeatureFactory
import kekmech.ru.feature_dashboard.elm.DashboardReducer

object DashboardModule : ModuleProvider({
    single { DashboardDependencies(get(), get(), get(), get(), get(), get()) } // он не просто так single!!!
    factory { DashboardFeatureFactory(get(), get()) }
    factory { DashboardReducer(get()) }
    factory { DashboardActor(get(), get(), get()) }
    factory { DashboardAnalytics(get()) }
})