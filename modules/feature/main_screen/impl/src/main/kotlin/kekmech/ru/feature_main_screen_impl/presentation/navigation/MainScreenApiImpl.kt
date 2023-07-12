package kekmech.ru.feature_main_screen_impl.presentation.navigation

import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenApi
import kekmech.ru.feature_main_screen_impl.presentation.screen.main.MainScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class MainScreenApiImpl : MainScreenApi {

    override fun navTarget(): NavTarget =
        MainScreenNavTarget()
}
