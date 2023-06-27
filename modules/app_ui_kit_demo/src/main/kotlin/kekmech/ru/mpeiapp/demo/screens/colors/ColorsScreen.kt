package kekmech.ru.mpeiapp.demo.screens.colors

import androidx.compose.runtime.Composable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ColorsScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ColorsScreen() }
}

@Composable
private fun ColorsScreen() {
    UiKitScreen(title = "Colors") {
        /* no-op */
    }
}
