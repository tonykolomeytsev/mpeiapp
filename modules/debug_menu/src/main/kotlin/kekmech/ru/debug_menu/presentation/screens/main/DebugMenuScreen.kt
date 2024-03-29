package kekmech.ru.debug_menu.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import kekmech.ru.ui_kit_dialogs.AlertDialog
import kekmech.ru.ui_kit_dialogs.AlertDialogState
import kekmech.ru.ui_kit_dialogs.rememberAlertDialogState
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.launch
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

@Suppress("LongMethod")
@Composable
private fun DebugMenuScreen(
    store: DebugMenuStore,
    state: DebugMenuState,
) {
    val backendEnvDialog = rememberAlertDialogState()
    val themeSelectionDialog = rememberAlertDialogState()
    val onAccept = store.rememberAcceptAction()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = "MpeiX Debug Menu")
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                BackendEnvironmentItem(
                    appEnvironment = state.appEnvironment,
                    onClick = { backendEnvDialog.showDialog() },
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
                    onClick = { themeSelectionDialog.showDialog() },
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
                val context = LocalContext.current
                val versionName = state.appVersionName.versionName
                AppVersionItem(
                    versionName = versionName,
                    onClick = {
                        context.copyToClipboard(versionName, label = "MpeiX version")
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(message = "MpeiX version copied")
                        }
                    }
                )
            }
        }
    }

    BackendEnvDialog(backendEnvDialog, onAccept)
    ThemeSelectionDialog(themeSelectionDialog, onAccept)
}

@Composable
private fun BackendEnvDialog(
    state: AlertDialogState,
    onAccept: ElmAcceptAction<DebugMenuEvent>,
) {
    AlertDialog(
        onDismissRequest = { state.hideDialog() },
        state = state,
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            item {
                Text(
                    text = "Select backend environment",
                    style = MpeixTheme.typography.header3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                )
            }
            items(AppEnvironment.values()) { env ->
                ListItem(
                    headlineText = env.name,
                    modifier = Modifier
                        .clickable {
                            state.hideDialog()
                            onAccept(Ui.Click.Environment(env))
                        },
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectionDialog(
    state: AlertDialogState,
    onAccept: ElmAcceptAction<DebugMenuEvent>,
) {
    AlertDialog(
        onDismissRequest = { state.hideDialog() },
        state = state,
    ) { innerPadding ->
        LazyColumn(Modifier.padding(innerPadding)) {
            item {
                Text(
                    text = "Select theme",
                    style = MpeixTheme.typography.header3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                )
            }
            items(AppTheme.values()) { theme ->
                ListItem(
                    headlineText = theme.name,
                    modifier = Modifier
                        .clickable {
                            state.hideDialog()
                            onAccept(Ui.Click.Theme(theme))
                        },
                )
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
private fun AppVersionItem(
    versionName: String,
    onClick: () -> Unit,
) {
    ListItem(
        headlineText = versionName,
        overlineText = "App version",
        modifier = Modifier
            .clickable(onClick = onClick),
    )
}
