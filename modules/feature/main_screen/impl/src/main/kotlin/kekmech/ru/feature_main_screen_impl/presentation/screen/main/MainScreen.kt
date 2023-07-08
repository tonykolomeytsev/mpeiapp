package kekmech.ru.feature_main_screen_impl.presentation.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.feature_main_screen_api.presentation.navigation.MainScreenTab
import kekmech.ru.lib_navigation_api.NavTarget
import kotlinx.parcelize.Parcelize

@Parcelize
internal class MainScreenNavTarget(private val tab: MainScreenTab) : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { modifier -> MainScreen(tab, modifier) }
}

@Composable
private fun MainScreen(
    tab: MainScreenTab,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Selected tab: $tab")
    }
}
