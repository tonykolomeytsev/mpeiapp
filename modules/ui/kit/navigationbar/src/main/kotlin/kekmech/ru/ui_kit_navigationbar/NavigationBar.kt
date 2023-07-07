package kekmech.ru.ui_kit_navigationbar

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import androidx.compose.material3.NavigationBarItem as Material3NavigationBarItem

/**
 * # NavigationBar
 *
 * Mapping implementation to [androidx.compose.material3.NavigationBar].
 * Navigation bars offer a persistent and convenient way to switch between primary destinations in
 * an app.
 *
 * [NavigationBar] should contain three to five [Material3NavigationBarItem]s, each representing a singular
 * destination. See [Material3NavigationBarItem] for configuration specific to each item, and not the overall
 * [NavigationBar] component.
 *
 * Usage:
 * ```kotlin
 * NavigationBar(
 *     modifier = Modifier.fillMaxWidth(),
 * ) {
 *     NavigationBarItem(
 *         selected = selectedItemIdx == 0,
 *         onClick = { selectedItemIdx = 0 },
 *         icon = {
 *             Icon(
 *                 painterSelected = MpeixIcons.WhatshotBlack24,
 *                 painterUnselected = MpeixIcons.WhatshotOutline24,
 *             )
 *         },
 *         label = "Dashboard",
 *     )
 *     // more items ...
 * }
 * ```
 *
 * @param modifier the [Modifier] to be applied to this navigation bar
 * @param windowInsets a window insets of the navigation bar.
 * @param content the content of this navigation bar, typically 3-5 [Material3NavigationBarItem]s
 */
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = NavigationBarDefaults.windowInsets,
    content: @Composable RowScope.() -> Unit,
) {
    androidx.compose.material3.NavigationBar(
        modifier = modifier,
        containerColor = MpeixTheme.palette.surface,
        contentColor = MpeixTheme.palette.content,
        tonalElevation = 0.dp,
        windowInsets = windowInsets,
        content = content,
    )
}

/**
 * # NavigationBarItem
 *
 * Mapping implementation to [androidx.compose.material3.NavigationBarItem].
 * Navigation bars offer a persistent and convenient way to switch between primary destinations in
 * an app.
 *
 * The recommended configuration for a [NavigationBarItem] depends on how many items there are
 * inside a [NavigationBar]:
 *
 * - Three destinations: Display icons and text labels for all destinations.
 * - Four destinations: Active destinations display an icon and text label. Inactive destinations
 * display icons, and text labels are recommended.
 * - Five destinations: Active destinations display an icon and text label. Inactive destinations
 * use icons, and use text labels if space permits.
 *
 * @param selected whether this item is selected
 * @param onClick called when this item is clicked
 * @param icon icon for this item, typically an [NavigationBarItemIconScope.Icon]
 * @param modifier the [Modifier] to be applied to this item
 * @param enabled controls the enabled state of this item. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param label optional text label for this item
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this item. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this item in different states.
 *
 * @see NavigationBarItemIconScope.Icon
 */
@Composable
fun RowScope.NavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable NavigationBarItemIconScope.() -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Material3NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            CompositionLocalProvider(
                LocalNavigationBarItemSelectedState provides selected,
            ) {
                icon.invoke(NavigationBarItemIconScope)
            }
        },
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = label,
            )
        },
        alwaysShowLabel = true,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MpeixTheme.palette.content,
            selectedTextColor = MpeixTheme.palette.content,
            indicatorColor = MpeixTheme.palette.primaryContainer,
            unselectedIconColor = MpeixTheme.palette.contentVariant,
            unselectedTextColor = MpeixTheme.palette.contentVariant,
            disabledIconColor = MpeixTheme.palette.contentDisabled,
            disabledTextColor = MpeixTheme.palette.contentDisabled,
        ),
        interactionSource = interactionSource,
    )
}

private val LocalNavigationBarItemSelectedState = compositionLocalOf<Boolean> {
    error("LocalNavigationBarItemSelectedState not provided")
}

object NavigationBarItemIconScope {

    @Composable
    fun Icon(
        painterSelected: Painter,
        painterUnselected: Painter,
    ) {
        Icon(
            painter = if (LocalNavigationBarItemSelectedState.current) {
                painterSelected
            } else {
                painterUnselected
            },
            contentDescription = null,
        )
    }

    @Composable
    fun Icon(
        imageVectorSelected: ImageVector,
        imageVectorUnselected: ImageVector,
    ) {
        Icon(
            imageVector = if (LocalNavigationBarItemSelectedState.current) {
                imageVectorSelected
            } else {
                imageVectorUnselected
            },
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun NavigationBarPreview() {
    MpeixTheme {
        NavigationBar {
            NavigationBarItem(
                selected = true,
                onClick = { /* no-op */ },
                icon = {
                    Icon(
                        imageVectorSelected = Icons.Filled.Star,
                        imageVectorUnselected = Icons.Outlined.Star,
                    )
                },
                label = "Stars",
            )
            NavigationBarItem(
                selected = false,
                onClick = { /* no-op */ },
                icon = {
                    Icon(
                        imageVectorSelected = Icons.Filled.Home,
                        imageVectorUnselected = Icons.Outlined.Home,
                    )
                },
                label = "Home",
            )
            NavigationBarItem(
                selected = false,
                onClick = { /* no-op */ },
                icon = {
                    Icon(
                        imageVectorSelected = Icons.Filled.AccountBox,
                        imageVectorUnselected = Icons.Outlined.AccountBox,
                    )
                },
                label = "Account",
            )
            NavigationBarItem(
                selected = false,
                onClick = { /* no-op */ },
                icon = {
                    Icon(
                        imageVectorSelected = Icons.Filled.Search,
                        imageVectorUnselected = Icons.Outlined.Search,
                    )
                },
                label = "Search",
            )
        }
    }
}
