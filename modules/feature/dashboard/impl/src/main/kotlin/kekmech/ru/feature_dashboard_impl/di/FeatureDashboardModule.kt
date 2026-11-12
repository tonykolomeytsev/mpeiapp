package kekmech.ru.feature_dashboard_impl.di

import kekmech.ru.ext_koin.bindIntoList
import kekmech.ru.feature_dashboard_impl.data.repository.WhatsNewBannerRepository
import kekmech.ru.feature_dashboard_impl.domain.interactor.GetUpcomingEventsInteractor
import kekmech.ru.feature_dashboard_impl.navigation.DashboardNavEntries
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardActor
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardStoreFactory
import kekmech.ru.lib_navigation.NavEntries
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val FeatureDashboardModule = module {
    // I do not remember why this object should be `single`, but this is really necessary
    singleOf(::DashboardDependencies)
    singleOf(::DashboardStoreFactory)
    factoryOf(::DashboardReducer)
    factoryOf(::DashboardActor)
    factoryOf(::GetUpcomingEventsInteractor)
    factoryOf(::WhatsNewBannerRepository)
    factoryOf(::DashboardNavEntries) bindIntoList NavEntries::class
}
