package kekmech.ru.debug_menu.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_elm.rememberAcceptAction
import kekmech.ru.common_kotlin.Resource
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Ui
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuState
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStore
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import kekmech.ru.domain_app_settings_models.AppEnvironment
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
    var openDialog by remember { mutableStateOf(false) }
    val accept = store.rememberAcceptAction()

    Scaffold(
        topBar = {
            TopAppBar(title = "MpeiX Debug Menu")
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item("environment") {
                Column {
                    ListItem(
                        headlineText = "Backend environment",
                        modifier = Modifier
                            .clickable { openDialog = true },
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
                    Text(
                        text = "For the 'Backend environment' setting to take effect, " +
                                "you need to restart the application",
                        style = MpeixTheme.typography.paragraphNormal,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                    )
                }
            }
        }
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MpeixTheme.palette.surface,
                contentColor = MpeixTheme.palette.content,
            ) {
                LazyColumn {
                    for (env in AppEnvironment.values()) {
                        item(env) {
                            ListItem(
                                headlineText = env.name,
                                modifier = Modifier
                                    .clickable {
                                        accept(Ui.Click.Environment(env))
                                        openDialog = false
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}
