package kekmech.ru.feature_map_impl.presentation.navigation

import kekmech.ru.feature_map_api.presentation.navigation.MapScreenApi
import kekmech.ru.feature_map_impl.presentation.screen.main.MapScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class MapScreenApiImpl : MapScreenApi {

    override fun navTarget(): NavTarget =
        MapScreenNavTarget()
}
