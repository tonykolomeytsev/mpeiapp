package kekmech.ru.debug_menu.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_kotlin.Resource
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuState
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStore
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
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
    Scaffold(
        topBar = {
            TopAppBar(title = "MpeiX Debug Menu")
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item("environment") {
                ListItem(
                    headlineText = "Backend environment",
                    modifier = Modifier
                        .clickable { /* no-op */ },
                    trailingContent = {
                        Text(
                            text = when (val env = state.appEnvironment) {
                                is Resource.Data -> env.value.name
                                is Resource.Loading -> "Loading..."
                                is Resource.Error -> "Error!"
                            },
                            style = MpeixTheme.typography.paragraphNormal,
                        )
                    }
                )
            }
        }
    }
}
