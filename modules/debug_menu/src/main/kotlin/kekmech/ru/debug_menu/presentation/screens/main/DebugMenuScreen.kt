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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.chuckerteam.chucker.api.Chucker
import kekmech.ru.common_elm.Resource
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_elm.rememberAcceptAction
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.debug_menu.R
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.FeatureTogglesNavTarget
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
        elmNode(
            buildContext = buildContext,
            storeFactoryClass = DebugMenuStoreFactory::class,
            factory = { create() },
        ) { store, state, _ -> DebugMenuScreen(store, state) }
}

@Composable
private fun DebugMenuScreen(
    store: DebugMenuStore,
    state: DebugMenuState,
) {
    var openBackendEnvironmentDialog by remember { mutableStateOf(false) }
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
                BackendEnvironmentItem(
                    appEnvironment = state.appEnvironment,
                    onClick = { openBackendEnvironmentDialog = true },
                )
            }
            item {
                FeatureTogglesItem()
            }
            item {
                ChuckerItem()
            }
        }
    }

    if (openBackendEnvironmentDialog) {
        AlertDialog(
            onDismissRequest = { openBackendEnvironmentDialog = false },
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
                                        openBackendEnvironmentDialog = false
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackendEnvironmentItem(
    appEnvironment: Resource<AppEnvironment>,
    onClick: () -> Unit,
) {
    Column {
        ListItem(
            headlineText = "Backend environment",
            modifier = Modifier
                .clickable(onClick = onClick),
            leadingContent = {
                Icon(painterResource(R.drawable.ic_category_24))
            },
            trailingContent = {
                Text(
                    text = when (appEnvironment) {
                        is Resource.Data -> appEnvironment.value.name
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
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 24.dp),
        )
    }
}

@Composable
private fun FeatureTogglesItem() {
    val navigator = LocalBackStackNavigator.current
    ListItem(
        headlineText = "Feature Toggles",
        leadingContent = {
            Icon(painterResource(R.drawable.ic_toggle_24))
        },
        modifier = Modifier
            .clickable { navigator.navigate(FeatureTogglesNavTarget()) },
    )
}

@Composable
private fun ChuckerItem() {
    val context = LocalContext.current
    ListItem(
        headlineText = "Launch Chucker",
        supportingText = "HTTP(s) requests/responses inspector",
        leadingContent = {
            Icon(painterResource(R.drawable.ic_public_24))
        },
        modifier = Modifier
            .clickable {
                context.startActivity(Chucker.getLaunchIntent(context))
            },
    )
}
