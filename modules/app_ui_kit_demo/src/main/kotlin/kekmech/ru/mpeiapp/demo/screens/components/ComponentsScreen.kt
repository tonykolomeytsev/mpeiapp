package kekmech.ru.mpeiapp.demo.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.common_navigation_compose.LocalBackStackNavigator
import kekmech.ru.mpeiapp.demo.screens.components.lists.ListItemScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.components.topappbar.ComponentsTopAppBarNavTarget
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ComponentsScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ComponentsScreen() }
}


@Composable
private fun ComponentsScreen() {
    val navigator = LocalBackStackNavigator.current

    UiKitScreen(title = "Components") { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item("ListItem") {
                SectionItem(
                    onClick = {
                        navigator.navigate(ListItemScreenNavTarget())
                    },
                    name = "ListItem",
                )
            }
            item("Top app bar") {
                SectionItem(
                    onClick = {
                        navigator.navigate(ComponentsTopAppBarNavTarget())
                    },
                    name = "Top app bar",
                )
            }
        }
    }
}
