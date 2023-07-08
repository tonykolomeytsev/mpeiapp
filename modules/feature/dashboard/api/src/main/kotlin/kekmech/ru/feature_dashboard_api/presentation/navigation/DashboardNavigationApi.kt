package kekmech.ru.feature_dashboard_api.presentation.navigation

import kekmech.ru.lib_navigation_api.NavTarget

interface DashboardNavigationApi {

    fun getNavTarget(): NavTarget
}
