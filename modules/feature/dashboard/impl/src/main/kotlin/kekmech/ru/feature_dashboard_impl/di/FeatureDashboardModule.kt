package kekmech.ru.feature_dashboard_impl.di

import kekmech.ru.feature_dashboard_api.DashboardFeatureLauncher
import kekmech.ru.feature_dashboard_api.presentation.navigation.DashboardScreenApi
import kekmech.ru.feature_dashboard_impl.domain.interactor.GetUpcomingEventsInteractor
import kekmech.ru.feature_dashboard_impl.launcher.DashboardFeatureLauncherImpl
import kekmech.ru.feature_dashboard_impl.presentation.navigation.DashboardScreenApiImpl
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardActor
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardStoreProvider
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
    factoryOf(::GetUpcomingEventsInteractor)
    factoryOf(::DashboardScreenApiImpl) bind DashboardScreenApi::class
}
