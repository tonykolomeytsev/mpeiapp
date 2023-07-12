package kekmech.ru.mpeiapp.presentation.navigation

import androidx.compose.runtime.Composable
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenDependencyComponent
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.lib_navigation_compose.node.backstack.BackStackNode

/**
 * Explicit backstack root declaration
 */
@Composable
internal fun NodeComponentActivity.ComposableAppRoot(
    authorizedNavTarget: () -> NavTarget,
    unauthorizedNavTarget: () -> NavTarget,
    authorized: Boolean,
    mainScreenDependencyComponent: MainScreenDependencyComponent,
) {

    NodeHost(integrationPoint = appyxIntegrationPoint) { buildContext ->
        object :
            BackStackNode(
                buildContext = buildContext,
                rootNavTarget = if (authorized) {
                    authorizedNavTarget.invoke()
                } else {
                    unauthorizedNavTarget.invoke()
                },
            ),
            MainScreenDependencyComponent by mainScreenDependencyComponent { /* no-op */ }
    }
}
