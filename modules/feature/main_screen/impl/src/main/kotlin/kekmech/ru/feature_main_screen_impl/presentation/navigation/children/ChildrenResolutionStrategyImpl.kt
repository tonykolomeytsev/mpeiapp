package kekmech.ru.feature_main_screen_impl.presentation.navigation.children

import androidx.compose.material3.Text
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.feature_dashboard_api.presentation.navigation.DashboardNavigationApi
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab
import kekmech.ru.feature_main_screen_api.presentation.navigation.children.ChildrenResolutionStrategy

internal class ChildrenResolutionStrategyImpl(
    private val dashboardNavigationApi: DashboardNavigationApi,
) : ChildrenResolutionStrategy {

    override fun resolve(navTarget: MainScreenTab, buildContext: BuildContext): Node =
        when (navTarget) {
            MainScreenTab.Dashboard ->
                dashboardNavigationApi.getNavTarget().resolve(buildContext)
            else -> node(buildContext) { Text(text = "Not implemented yet!") }
        }
}
