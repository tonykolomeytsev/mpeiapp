package kekmech.ru.ui_kit_topappbar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_icons.MpeixIcons
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # TopAppBar
 *
 * Mapping implementation to [androidx.compose.material3.TopAppBar].
 * Top app bars display information and actions at the top of a screen.
 *
 * This small TopAppBar has slots for a title, navigation icon, and actions.
 *
 * ![Small top app bar image](https://developer.android.com/images/reference/androidx/compose/material3/small-top-app-bar.png)
 *
 * @param title the title to be displayed in the top app bar
 * @param modifier the [Modifier] to be applied to this top app bar
 * @param navigationIcon the navigation icon displayed at the start of the top app bar. This should
 * typically be one of:
 *   - [TopAppBarNavigationIconScope.BackIconButton]
 *   - [TopAppBarNavigationIconScope.CloseIconButton]
 * @param actions the actions displayed at the end of the top app bar. This should typically be
 * [IconButton]s. The default layout here is a [Row], so icons inside will be placed horizontally.
 * @param windowInsets a window insets that app bar will respect.
 * @param scrollBehavior a [TopAppBarScrollBehavior] which holds various offset values that will be
 * applied by this top app bar to set up its height and colors. A scroll behavior is designed to
 * work in conjunction with a scrolled content to change the top app bar appearance as the content
 * scrolls. See [TopAppBarScrollBehavior.nestedScrollConnection].
 *
 * @see androidx.compose.material3.TopAppBar
 */
@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable TopAppBarNavigationIconScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    androidx.compose.material3.TopAppBar(
        title = {
            Text(
                text = title,
                style = MpeixTheme.typography.header3,
                color = MpeixTheme.palette.content,
            )
        },
        modifier = modifier,
        navigationIcon = { navigationIcon.invoke(TopAppBarNavigationIconScope) },
        actions = actions,
        windowInsets = windowInsets,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MpeixTheme.palette.background,
            scrolledContainerColor = MpeixTheme.palette.surface,
            navigationIconContentColor = MpeixTheme.palette.content,
            titleContentColor = MpeixTheme.palette.content,
            actionIconContentColor = MpeixTheme.palette.content,
        ),
        scrollBehavior = scrollBehavior,
    )
}

object TopAppBarNavigationIconScope {

    @Composable
    fun BackIconButton(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                painter = MpeixIcons.ArrowBack24,
                contentDescription = "Back",
            )
        }
    }

    @Composable
    fun CloseIconButton(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                painter = MpeixIcons.Close24,
                contentDescription = "Close",
            )
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    MpeixTheme {
        Column {
            TopAppBar(title = "Title")
            Spacer(modifier = Modifier.height(16.dp))
            TopAppBar(
                title = "Title",
                navigationIcon = { BackIconButton { /* no-op */ } },
            )
            Spacer(modifier = Modifier.height(16.dp))
            TopAppBar(
                title = "Title",
                navigationIcon = { CloseIconButton { /* no-op */ } },
            )
        }
    }
}
