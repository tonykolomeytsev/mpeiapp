package kekmech.ru.ui_kit_navigationbar.floating

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # FloatingNavBar
 *
 * iOS-like floating navigation bar: a pill-shaped container with a shadow, that holds
 * equally sized tabs. Fills available width on phones, on wide screens is limited
 * to [FloatingNavBarForm.maxWidth] and centered horizontally. The selected tab is highlighted with a pill-shaped background.
 *
 * Based on the expanded state of
 * [compose-floating-tab-bar](https://github.com/elyesmansour/compose-floating-tab-bar).
 *
 * Usage:
 * ```kotlin
 * FloatingNavBar(
 *     modifier = Modifier.padding(horizontal = 16.dp),
 * ) {
 *     Tab(
 *         selected = selectedItemIdx == 0,
 *         onClick = { selectedItemIdx = 0 },
 *         iconSelected = Icons.Filled.Home,
 *         iconUnselected = Icons.Outlined.Home,
 *         label = "Home",
 *     )
 *     // more tabs ...
 * }
 * ```
 *
 * @param modifier the [Modifier] to be applied to this navigation bar
 * @param palette colors of the navigation bar
 * @param form shapes and sizes of the navigation bar
 * @param content the content of this navigation bar, typically 3-5 [FloatingNavBarScope.Tab]s
 */
@Composable
public fun FloatingNavBar(
    modifier: Modifier = Modifier,
    palette: FloatingNavBarPalette = FloatingNavBarPalette.Default,
    form: FloatingNavBarForm = FloatingNavBarForm.Default,
    content: @Composable FloatingNavBarScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .adaptiveContentWidth(maxWidth = form.maxWidth)
            .shadow(
                elevation = form.elevation,
                shape = form.shape,
            )
            .background(
                color = palette.background,
                shape = form.shape,
            )
            .clip(form.shape)
            .padding(form.contentPadding)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(form.tabSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val scope = remember(this, palette, form) { FloatingNavBarScope(this, palette, form) }
        scope.content()
    }
}

public class FloatingNavBarScope internal constructor(
    rowScope: RowScope,
    private val palette: FloatingNavBarPalette,
    private val form: FloatingNavBarForm,
) : RowScope by rowScope {

    /**
     * Tab of the [FloatingNavBar]. All tabs share the width of the navigation bar equally.
     *
     * @param selected whether this tab is selected
     * @param onClick called when this tab is clicked
     * @param iconSelected icon displayed when the tab is selected
     * @param iconUnselected icon displayed when the tab is not selected
     * @param label text label for this tab
     * @param modifier the [Modifier] to be applied to this tab
     */
    @Composable
    public fun Tab(
        selected: Boolean,
        onClick: () -> Unit,
        iconSelected: ImageVector,
        iconUnselected: ImageVector,
        label: String,
        modifier: Modifier = Modifier,
    ) {
        val form = form
        val backgroundColor by animateColorAsState(
            targetValue = if (selected) palette.selectedTabBackground else palette.background.copy(alpha = 0f),
            label = "FloatingNavBarTabBackground",
        )
        val contentColor by animateColorAsState(
            targetValue = if (selected) palette.selectedContent else palette.unselectedContent,
            label = "FloatingNavBarTabContent",
        )
        Column(
            modifier = modifier
                .weight(1f)
                .clip(form.tabShape)
                .background(
                    color = backgroundColor,
                    shape = form.tabShape,
                )
                .clickable(
                    role = Role.Tab,
                    onClick = onClick,
                )
                .semantics { this.selected = selected }
                .padding(form.tabContentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = if (selected) iconSelected else iconUnselected,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(form.iconSize),
            )
            Text(
                text = label,
                color = contentColor,
                style = form.labelTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
@Preview
private fun FloatingNavBarPreview() {
    MpeixTheme {
        Box(
            modifier = Modifier
                .background(MpeixTheme.palette.surfaceContainer)
                .padding(16.dp),
        ) {
            var selectedItemIdx by remember { mutableIntStateOf(0) }
            FloatingNavBar {
                Tab(
                    selected = selectedItemIdx == 0,
                    onClick = { selectedItemIdx = 0 },
                    iconSelected = Icons.Filled.Home,
                    iconUnselected = Icons.Outlined.Home,
                    label = "Главная",
                )
                Tab(
                    selected = selectedItemIdx == 1,
                    onClick = { selectedItemIdx = 1 },
                    iconSelected = Icons.Filled.DateRange,
                    iconUnselected = Icons.Outlined.DateRange,
                    label = "Расписание",
                )
                Tab(
                    selected = selectedItemIdx == 2,
                    onClick = { selectedItemIdx = 2 },
                    iconSelected = Icons.Filled.LocationOn,
                    iconUnselected = Icons.Outlined.LocationOn,
                    label = "Карта",
                )
                Tab(
                    selected = selectedItemIdx == 3,
                    onClick = { selectedItemIdx = 3 },
                    iconSelected = Icons.Filled.AccountCircle,
                    iconUnselected = Icons.Outlined.AccountCircle,
                    label = "Профиль",
                )
            }
        }
    }
}
