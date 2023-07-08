package kekmech.ru.feature_main_screen_impl.presentation.navigation

import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenNavigationApi
import kekmech.ru.feature_main_screen_impl.presentation.screen.main.MainScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class MainScreenNavigationApiImpl : MainScreenNavigationApi {

    override fun getNavTarget(): NavTarget =
        MainScreenNavTarget()
}
