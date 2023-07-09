package kekmech.ru.feature_main_screen_impl.presentation.navigation.children

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.feature_bars_api.presentation.navigation.BarsScreenApi
import kekmech.ru.feature_dashboard_api.presentation.navigation.DashboardScreenApi
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab
import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy
import kekmech.ru.feature_map_api.presentation.navigation.MapScreenApi
import kekmech.ru.feature_schedule_api.presentation.navigation.ScheduleScreenApi

internal class ChildrenResolutionStrategyImpl(
    private val dashboardScreenApi: DashboardScreenApi,
    private val scheduleScreenApi: ScheduleScreenApi,
    private val mapScreenApi: MapScreenApi,
    private val barsScreenApi: BarsScreenApi,
) : ChildrenResolutionStrategy {

    override fun resolve(navTarget: MainScreenTab, buildContext: BuildContext): Node =
        when (navTarget) {
            MainScreenTab.Dashboard -> dashboardScreenApi.navTarget()
            MainScreenTab.Schedule -> scheduleScreenApi.navTarget()
            MainScreenTab.Map -> mapScreenApi.navTarget()
            MainScreenTab.Profile -> barsScreenApi.navTarget()
        }.resolve(buildContext)
}
