package kekmech.ru.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_icons.MpeixIcons
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # DockedSearchBar
 *
 * Mapping implementation to [androidx.compose.material3.DockedSearchBar]
 * A search bar represents a floating search field that allows users to enter a
 * keyword or phrase and get relevant information.
 *
 * ![Search bar image](https://developer.android.com/images/reference/androidx/compose/material3/docked-search-bar.png)
 *
 * Usage example:
 *
 * ```kotlin
 * DockedSearchBar(
 *      query = "Search query",
 *      active = true,
 *      onQueryChange = { /* update query */ },
 *      onSearch = { /* update content view */ },
 *      onActiveChanged = { /* update active parameter */ },
 *      onRemoveQuery = { /* remove current query */ },
 * ){/* searched content */}
 * ```
 *
 * @param query the query text to be shown in the search bar's input field
 * @param active whether this search bar is active
 * @param onQueryChange the callback to be invoked when the input service updates the query.
 * An updated text comes as a parameter of the callback.
 * @param onSearch the callback to be invoked when the input service triggers the ImeAction.
 * Search action. The current query comes as a parameter of the callback.
 * @param onActiveChanged the callback to be invoked when this search bar's active state is changed
 * @param onRemoveQuery the callback to be invoked when search bar was cleaned
 * @param modifier the Modifier to be applied to this search bar
 * @param placeholder the placeholder to be displayed when the search bar's query is empty.
 * @param enabled controls the enabled state of this search bar. When false, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility services.
 * @param content the content of this search bar that will be displayed below the input field
 */

@Composable
fun DockedSearchBar(
    query: String,
    active: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onActiveChanged: (Boolean) -> Unit,
    onRemoveQuery: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    androidx.compose.material3.DockedSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        enabled = enabled,
        onActiveChange = onActiveChanged,
        modifier = modifier,
        leadingIcon = { Icon(painter = MpeixIcons.Search24, contentDescription = "Search") },
        trailingIcon = if (query.isNotBlank()) {
            {
                IconButton(onClick = onRemoveQuery) {
                    Icon(painter = MpeixIcons.Close24, contentDescription = "Close")
                }
            }
        } else {
            null
        },
        colors = SearchBarDefaults.colors(
            containerColor = MpeixTheme.palette.surface,
            dividerColor = MpeixTheme.palette.outline,
            inputFieldColors = inputFieldColors(
                focusedTextColor = MpeixTheme.palette.content,
                unfocusedTextColor = MpeixTheme.palette.content,
                disabledTextColor = MpeixTheme.palette.contentDisabled,
                cursorColor = MpeixTheme.palette.primary,
                focusedLeadingIconColor = MpeixTheme.palette.contentVariant,
                unfocusedLeadingIconColor = MpeixTheme.palette.contentVariant,
                disabledLeadingIconColor = MpeixTheme.palette.content.copy(alpha = SearchBarTokens.DisabledAlpha),
                focusedTrailingIconColor = MpeixTheme.palette.contentVariant,
                unfocusedTrailingIconColor = MpeixTheme.palette.contentVariant,
                disabledTrailingIconColor = MpeixTheme.palette.contentVariant.copy(alpha = SearchBarTokens.DisabledAlpha),
                focusedPlaceholderColor = MpeixTheme.palette.surface,
                unfocusedPlaceholderColor = MpeixTheme.palette.surface,
                disabledPlaceholderColor = MpeixTheme.palette.surface.copy(alpha = SearchBarTokens.DisabledAlpha),
            ),
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MpeixTheme.typography.paragraphBig,
                color = MpeixTheme.palette.contentVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        content = content,
        // helps to avoid primary color overlay on top of the container
        tonalElevation = 0.dp,
    )
}

@Preview
@Composable
fun SearchBarPreview() {
    MpeixTheme {
        Column {
            DockedSearchBar(
                query = "Query",
                active = false,
                onQueryChange = {},
                onSearch = {},
                onActiveChanged = {},
                onRemoveQuery = {},
            )
            Spacer(modifier = Modifier.height(16.dp))
            DockedSearchBar(
                query = "",
                active = false,
                onQueryChange = {},
                onSearch = {},
                onActiveChanged = {},
                onRemoveQuery = {},
                placeholder = "Placeholder",
            )
        }
    }
}
