package kekmech.ru.mpeiapp.demo.screens.components.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.search.DockedSearchBar
import kekmech.ru.ui_kit_lists.ListItem
import kotlinx.parcelize.Parcelize

@Parcelize
internal class DockedSearchBarScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { DockedSearchBarScreen() }


}

@Composable
private fun DockedSearchBarScreen() {
    UiKitScreen(title = "DockedSearchBar") { innerPadding ->
        val data = remember { listOf("First", "Second", "Third") }
        var query by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item("Docked search bar") {
                DockedSearchBar(
                    modifier = Modifier.fillMaxWidth(),
                    query = query,
                    active = active,
                    onQueryChange = { query = it },
                    onSearch = { active = false },
                    onActiveChanged = { active = it },
                    onRemoveQuery = { query = "" },
                    content = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(data.filter {
                                it.startsWith(
                                    prefix = query,
                                    ignoreCase = true
                                )
                            }) {
                                ListItem(headlineText = it)
                            }
                        }
                    }
                )
            }
        }
    }
}