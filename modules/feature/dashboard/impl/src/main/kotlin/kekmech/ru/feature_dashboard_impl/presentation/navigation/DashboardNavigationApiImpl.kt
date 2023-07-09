package kekmech.ru.feature_dashboard_impl.presentation.navigation

import kekmech.ru.feature_dashboard_api.presentation.navigation.DashboardNavigationApi
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.DashboardScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class DashboardNavigationApiImpl : DashboardNavigationApi {

    override fun getNavTarget(): NavTarget =
        DashboardScreenNavTarget()
}
