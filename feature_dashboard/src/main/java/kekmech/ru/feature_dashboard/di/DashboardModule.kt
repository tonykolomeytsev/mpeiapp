package kekmech.ru.feature_dashboard.di

import kekmech.ru.common_di.ModuleProvider
import kekmech.ru.feature_dashboard.presentation.DashboardActor
import kekmech.ru.feature_dashboard.presentation.DashboardFeatureFactory
import org.koin.dsl.bind

object DashboardModule : ModuleProvider({
    single { DashboardDependencies(get()) } bind DashboardDependencies::class
    single { DashboardFeatureFactory(get()) } bind DashboardFeatureFactory::class
    single { DashboardActor(get()) } bind DashboardActor::class
})