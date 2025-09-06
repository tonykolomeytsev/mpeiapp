package kekmech.ru.mpeiapp.demo.screens.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kekmech.ru.mpeiapp.demo.navigation.LocalNavBackStack
import kekmech.ru.mpeiapp.demo.navigation.NavScreen
import kekmech.ru.mpeiapp.demo.screens.components.lists.ListItemScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.components.navigationbar.NavigationBarScreenNavTarget
import kekmech.ru.mpeiapp.demo.screens.components.topappbar.ComponentsTopAppBarNavTarget
import kekmech.ru.mpeiapp.demo.ui.SectionItem
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kotlinx.serialization.Serializable

@Serializable
internal object ComponentsScreen : NavScreen {

    @Composable
    override fun Content() {
        ComponentsScreen()
    }
}


@Composable
private fun ComponentsScreen() {
    val backStack = LocalNavBackStack.current

    UiKitScreen(title = "Components") { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item("ListItem") {
                SectionItem(
                    onClick = {
                        backStack.add(ListItemScreenNavTarget)
                    },
                    name = "ListItem",
                )
            }
            item("Navigation bar") {
                SectionItem(
                    onClick = {
                        backStack.add(NavigationBarScreenNavTarget)
                    },
                    name = "Navigation bar",
                )
            }
            item("Top app bar") {
                SectionItem(
                    onClick = {
                        backStack.add(ComponentsTopAppBarNavTarget)
                    },
                    name = "Top app bar",
                )
            }
        }
    }
}
