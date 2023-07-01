package kekmech.ru.debug_menu.presentation.screens.feature_toggles

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import kekmech.ru.common_elm.elmNode
import kekmech.ru.common_elm.rememberAcceptAction
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesEvent.Ui
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesState
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesStore
import kekmech.ru.debug_menu.presentation.screens.feature_toggles.elm.FeatureTogglesStoreFactory
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_switch.Switch
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.parcelize.Parcelize

@Parcelize
internal class FeatureTogglesNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        elmNode(
            buildContext = buildContext,
            storeFactoryClass = FeatureTogglesStoreFactory::class,
            factory = { create() },
        ) { store, state, _ -> FeatureTogglesScreen(store, state) }
}

@Composable
private fun FeatureTogglesScreen(
    store: FeatureTogglesStore,
    state: FeatureTogglesState,
) {
    val navigator = LocalBackStackNavigator.current
    val onAccept = store.rememberAcceptAction()

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Feature Toggles",
                navigationIcon = {
                    BackIconButton { navigator.back() }
                },
            )
        }
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
                                    val interval = 4.dp.toPx()
                                    drawRect(
                                        color = primaryColor,
                                        style = Stroke(
                                            width = 2.dp.toPx(),
                                            pathEffect = PathEffect.dashPathEffect(
                                                intervals = floatArrayOf(interval, interval),
                                            )
                                        )
                                    )
                                }
                            }
                    )
                }
            }
        }
    }
}
