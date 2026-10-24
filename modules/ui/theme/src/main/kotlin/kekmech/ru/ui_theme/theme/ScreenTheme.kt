package kekmech.ru.ui_theme.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Widest content column of a full-screen Compose screen.
 * Matches `@dimen/tablet_frame_width` that the View based screens use on tablets.
 */
public val ScreenContentMaxWidth: Dp = 520.dp

/**
 * [MpeixTheme] for a full-screen destination that is also readable on tablets:
 * on wide windows (tablets in portrait / landscape, split screen, unfolded foldables) the content is
 * limited to [maxContentWidth] and centered, the free space on the sides gets the surface color.
 * On phones the content fills the window exactly as before.
 */
@Composable
public fun MpeixScreenTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    maxContentWidth: Dp = ScreenContentMaxWidth,
    content: @Composable () -> Unit,
) {
    MpeixTheme(darkTheme = darkTheme) {
        ConstrainedContent(maxContentWidth = maxContentWidth, content = content)
    }
}

/** Centers [content] in a column no wider than [maxContentWidth]. Must be called inside [MpeixTheme]. */
@Composable
public fun ConstrainedContent(
    modifier: Modifier = Modifier,
    maxContentWidth: Dp = ScreenContentMaxWidth,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MpeixTheme.palette.surface),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = maxContentWidth)
                .fillMaxWidth()
                .background(MpeixTheme.palette.background),
        ) {
            content()
        }
    }
}
