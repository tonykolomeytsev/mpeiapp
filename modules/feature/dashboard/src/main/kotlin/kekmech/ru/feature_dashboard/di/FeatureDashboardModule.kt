package kekmech.ru.feature_dashboard.di

import kekmech.ru.domain_dashboard.DashboardFeatureLauncher
import kekmech.ru.feature_dashboard.launcher.DashboardFeatureLauncherImpl
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardActor
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardStoreProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FeatureDashboardModule = module {
    // I do not remember why this object should be `single`, but this is really necessary
    singleOf(::DashboardDependencies)
    singleOf(::DashboardStoreProvider)
    factoryOf(::DashboardReducer)
    factoryOf(::DashboardActor)
    factoryOf(::DashboardFeatureLauncherImpl) bind DashboardFeatureLauncher::class
}
