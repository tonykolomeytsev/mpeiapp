package kekmech.ru.mpeiapp.demo.screens.components.navigationbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.lib_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_icons.MpeixIcons
import kekmech.ru.ui_kit_navigationbar.NavigationBar
import kekmech.ru.ui_kit_navigationbar.NavigationBarItem
import kotlinx.parcelize.Parcelize

@Parcelize
internal class NavigationBarScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { NavigationBarScreen() }
}

@Suppress("LongMethod", "MagicNumber")
@Composable
private fun NavigationBarScreen() {
    UiKitScreen(
        title = "Navigation bar",
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Top),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            var selectedItemIdx by remember { mutableStateOf(0) }
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
            ) {
                NavigationBarItem(
                    selected = selectedItemIdx == 0,
                    onClick = { selectedItemIdx = 0 },
                    icon = {
                        Icon(
                            painterSelected = MpeixIcons.WhatshotBlack24,
                            painterUnselected = MpeixIcons.WhatshotOutline24,
                        )
                    },
                    label = "Главная",
                )
                NavigationBarItem(
                    selected = selectedItemIdx == 1,
                    onClick = { selectedItemIdx = 1 },
                    icon = {
                        Icon(
                            painterSelected = MpeixIcons.EventBlack24,
                            painterUnselected = MpeixIcons.EventOutline24,
                        )
                    },
                    label = "Расписание",
                )
                NavigationBarItem(
                    selected = selectedItemIdx == 2,
                    onClick = { selectedItemIdx = 2 },
                    icon = {
                        Icon(
                            painterSelected = MpeixIcons.ExploreBlack24,
                            painterUnselected = MpeixIcons.ExploreOutline24,
                        )
                    },
                    label = "Карта",
                )
                NavigationBarItem(
                    selected = selectedItemIdx == 3,
                    onClick = { selectedItemIdx = 3 },
                    icon = {
                        Icon(
                            painterSelected = MpeixIcons.AccountBlack24,
                            painterUnselected = MpeixIcons.AccountOutline24,
                        )
                    },
                    label = "Профиль",
                )
            }
        }
    }
}
