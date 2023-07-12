package kekmech.ru.feature_onboarding_impl.presentation.screen

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
internal class WelcomeScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { modifier -> WelcomeScreen(modifier) }
}

@Composable
private fun WelcomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Welcome screen",
            style = MpeixTheme.typography.header2,
        )
    }
}
