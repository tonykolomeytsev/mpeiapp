package kekmech.ru.mpeiapp.demo.screens.components.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.node
import kekmech.ru.common_navigation_api.NavTarget
import kekmech.ru.mpeiapp.demo.ui.UiKitScreen
import kekmech.ru.ui_icons.MpeixIcons
import kekmech.ru.ui_kit_lists.ListItem
import kotlinx.parcelize.Parcelize

@Parcelize
internal class ListItemScreenNavTarget : NavTarget {

    override fun resolve(buildContext: BuildContext): Node =
        node(buildContext) { ListItemScreen() }
}

@Composable
private fun ListItemScreen() {
    UiKitScreen(title = "ListItem") { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            // Text only
            item {
                ListItem(headlineText = "One-line list item")
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    supportingText = "Supporting text",
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    overlineText = "Overline text",
                )
            }
            item {
                ListItem(
                    headlineText = "Three-line list item",
                    overlineText = "Overline text",
                    supportingText = "Supporting text",
                )
            }
            item {
                ListItem(
                    headlineText = "Three-line list item",
                    overlineText = "",
                    supportingText = "Supporting text\nWith second line",
                )
            }
            item { Divider() }

            // Text + Leading icon
            item() {
                ListItem(
                    headlineText = "One-line list item",
                    leadingContent = {
                        Icon(MpeixIcons.PersonOutline24)
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Icon(MpeixIcons.GroupsOutline24)
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    overlineText = "Overline text",
                    leadingContent = {
                        Icon(MpeixIcons.PersonOutline24)
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Three-line list item",
                    overlineText = "Overline text",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Icon(MpeixIcons.GroupsOutline24)
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item { Divider() }

            // Text + Leading monogram
            item {
                ListItem(
                    headlineText = "One-line list item",
                    leadingContent = {
                        Monogram(letter = 'A')
                    },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Monogram(letter = 'B')
                    },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    overlineText = "Overline text",
                    leadingContent = {
                        Monogram(letter = 'C')
                    },
                )
            }
            item {
                ListItem(
                    headlineText = "Three-line list item",
                    overlineText = "Overline text",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Monogram(letter = 'D')
                    },
                )
            }
            item { Divider() }

            // Text + Leading icon + Trailing text
            item {
                ListItem(
                    headlineText = "One-line list item",
                    leadingContent = {
                        Monogram(letter = 'A')
                    },
                    trailingContent = {
                        SupportingText(text = "100+")
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Monogram(letter = 'B')
                    },
                    trailingContent = {
                        SupportingText(text = "146%")
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Two-line list item",
                    overlineText = "Overline text",
                    leadingContent = {
                        Monogram(letter = 'C')
                    },
                    trailingContent = {
                        SupportingText(text = "Hello...")
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
            item {
                ListItem(
                    headlineText = "Three-line list item",
                    overlineText = "Overline text",
                    supportingText = "Supporting text",
                    leadingContent = {
                        Monogram(letter = 'D')
                    },
                    trailingContent = {
                        SupportingText(text = "Support")
                    },
                    modifier = Modifier.clickable { /* no-op */ },
                )
            }
        }
    }
}
