package kekmech.ru.feature_dashboard_impl.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.parcelize.Parcelize

@Parcelize
internal class DashboardScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { modifier -> DashboardScreen(modifier) }
}

@Composable
private fun DashboardScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Dashboard",
            style = MpeixTheme.typography.header2,
        )
    }
}
