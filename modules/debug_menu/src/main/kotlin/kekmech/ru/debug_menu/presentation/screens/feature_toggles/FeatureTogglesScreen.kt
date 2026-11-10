package kekmech.ru.debug_menu.presentation.screens.feature_toggles

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kekmech.ru.debug_menu.R
import kekmech.ru.debug_menu.presentation.navigation.LocalNavBackStack
import kekmech.ru.debug_menu.presentation.navigation.NavScreen
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEffect
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Ui
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesState
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesStoreFactory
import kekmech.ru.lib_elm_compose.ElmContent
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_switch.Switch
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
internal object FeatureTogglesScreen : NavScreen {

    @Composable
    override fun Content() {
        val factory = koinInject<FeatureTogglesStoreFactory>()
        ElmContent(
            key = "FeatureToggleScreen",
            storeFactory = { factory.create() },
            content = ::FeatureTogglesScreen,
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun FeatureTogglesScreen(
    onAccept: (FeatureTogglesEvent) -> Unit,
    state: FeatureTogglesState,
    @Suppress("Unused")
    effects: Flow<FeatureTogglesEffect>,
) {
    val backStack = LocalNavBackStack.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Feature Toggles",
                navigationIcon = {
                    BackIconButton { backStack.removeLastOrNull() }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Reset") },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_reset_24),
                        contentDescription = null,
                    )
                },
                onClick = { onAccept(Ui.Click.Reset) },
            )
        },
    ) { innerPadding ->
        val primaryColor = MpeixTheme.palette.primary
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            for (ft in state.featureToggles.value.orEmpty()) {
                item(ft.name) {
                    ListItem(
                        headlineText = ft.name,
                        trailingContent = {
                            Switch(
                                checked = ft.value,
                                onCheckedChange = {
                                    onAccept(Ui.Click.Switch(ft.name, it))
                                },
                            )
                        },
                        modifier = Modifier
                            .drawWithContent {
                                drawContent()
                                if (ft.overwritten) {
                                    val dp2 = 2.dp.toPx()
                                    val dp4 = 4.dp.toPx()
                                    drawRect(
                                        color = primaryColor,
                                        topLeft = Offset(dp2, dp2),
                                        size = size.copy(
                                            width = size.width - dp4,
                                            height = size.height - dp4,
                                        ),
                                        style = Stroke(width = dp2)
                                    )
                                }
                            }
                    )
                }
            }
        }
    }
}
