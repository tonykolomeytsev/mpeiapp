package kekmech.ru.feature_bars_impl.presentation.navigation

import kekmech.ru.feature_bars_api.presentation.navigation.BarsScreenApi
import kekmech.ru.feature_bars_impl.presentation.screen.main.BarsScreenNavTarget
import kekmech.ru.lib_navigation_api.NavTarget

internal class BarsScreenApiImpl : BarsScreenApi {

    override fun navTarget(): NavTarget =
        BarsScreenNavTarget()
}
