package kekmech.ru.debug_menu.presentation.screens.main

import androidx.compose.runtime.Composable
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuState
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStore
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import kotlinx.parcelize.Parcelize

@Parcelize
internal class DebugMenuNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        elmNode<DebugMenuStoreFactory, _, _, _>(
            buildContext = buildContext,
            factory = { create() },
        ) { _, store, state -> DebugMenuScreen(store, state) }
}

@Composable
private fun DebugMenuScreen(
    store: DebugMenuStore,
    state: DebugMenuState,
) {

}
