package kekmech.ru.debug_menu.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.chuckerteam.chucker.api.Chucker
import kekmech.ru.debug_menu.R
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.FeatureTogglesNavTarget
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuEvent.Ui
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuState
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStore
import kekmech.ru.debug_menu.presentation.screens.main.elm.DebugMenuStoreFactory
import kekmech.ru.ext_android.copyToClipboard
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.lib_elm.ElmAcceptAction
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.elmNode
import kekmech.ru.lib_elm.rememberAcceptAction
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.lib_navigation_compose.LocalBackStackNavigator
import kekmech.ru.lib_network.AppEnvironment
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
    val showBackendEnvDialog = remember { mutableStateOf(false) }
    val showThemeSelectionDialog = remember { mutableStateOf(false) }
    val onAccept = store.rememberAcceptAction()

    Scaffold(
        topBar = {
            TopAppBar(title = "MpeiX Debug Menu")
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                BackendEnvironmentItem(
                    appEnvironment = state.appEnvironment,
                    onClick = { showBackendEnvDialog.value = true },
                )
            }
            item {
                FeatureTogglesItem()
            }
            item {
                ChuckerItem()
            }
            item {
                ThemeSelectionItem(
                    state.appTheme,
                    onClick = { showThemeSelectionDialog.value = true },
                )
            }
            item {
                Text(
                    text = "Build info",
                    style = MpeixTheme.typography.header3,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp, bottom = 12.dp)
                )
            }
            item {
                AppVersionItem(versionName = state.appVersionName.versionName)
            }
        }
    }

    BackendEnvDialog(showBackendEnvDialog, onAccept)
    ThemeSelectionDialog(showThemeSelectionDialog, onAccept)
}

@Composable
private fun BackendEnvDialog(
    showDialog: MutableState<Boolean>,
    onAccept: ElmAcceptAction<DebugMenuEvent>,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
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
                                        onAccept(Ui.Click.Environment(env))
                                        showDialog.value = false
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
private fun ThemeSelectionDialog(
    showDialog: MutableState<Boolean>,
    onAccept: ElmAcceptAction<DebugMenuEvent>,
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
        ) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = MpeixTheme.palette.surface,
                contentColor = MpeixTheme.palette.content,
            ) {
                LazyColumn {
                    items(AppTheme.values()) { theme ->
                        ListItem(
                            headlineText = theme.name,
                            modifier = Modifier
                                .clickable {
                                    onAccept(Ui.Click.Theme(theme))
                                    showDialog.value = false
                                },
                        )
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

@Composable
private fun ThemeSelectionItem(
    appTheme: Resource<AppTheme>,
    onClick: () -> Unit,
) {
    ListItem(
        headlineText = "Selected theme",
        leadingContent = {
            Icon(painterResource(R.drawable.ic_color_lens_24))
        },
        trailingContent = {
            Text(
                text = when (appTheme) {
                    is Resource.Data -> appTheme.value.toString()
                    is Resource.Loading -> "Loading..."
                    is Resource.Error -> "Error!"
                },
                style = MpeixTheme.typography.paragraphNormal,
            )
        },
        modifier = Modifier
            .clickable(onClick = onClick),
    )
}

@Composable
private fun AppVersionItem(versionName: String) {
    val context = LocalContext.current
    val onClick: () -> Unit = {
        context.copyToClipboard(versionName, label = "MpeiX version")
    }
    ListItem(
        headlineText = versionName,
        overlineText = "App version",
        modifier = Modifier
            .clickable(onClick = onClick),
    )
}
