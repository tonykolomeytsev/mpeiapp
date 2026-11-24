package kekmech.ru.ui_kit_navigationbar.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.tappableElement
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_navigationbar.floating.FloatingNavBarPalette
import kekmech.ru.ui_kit_navigationbar.floating.FloatingNavBarScrim
import kekmech.ru.ui_kit_navigationbar.floating.scrimBrush
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # SystemNavBarScrim
 *
 * Scrim under the 3-button system navigation bar, so the buttons stay readable over the content
 * scrolled under them. The system bar itself is transparent: the app is drawn edge-to-edge
 * and doesn't let the system draw its own scrim.
 *
 * The scrim is as tall as the 3-button bar and isn't drawn at all with gesture navigation.
 * It fades in at the top of the bar, so the content laid out right above the bar
 * with `navigationBarsPadding()` is never covered. The color is the same as in [FloatingNavBarScrim].
 *
 * Place the scrim at the bottom of the screen, on top of the content:
 * ```kotlin
 * Box(Modifier.fillMaxSize()) {
 *     content()
 *     SystemNavBarScrim(Modifier.align(Alignment.BottomCenter))
 * }
 * ```
 *
 * The scrim doesn't consume touches.
 *
 * @param modifier the [Modifier] to be applied to this scrim
 */
@Composable
public fun SystemNavBarScrim(modifier: Modifier = Modifier) {
    // Tappable element insets are empty with gesture navigation: the gesture area isn't tappable
    SystemNavBarScrimContent(modifier.windowInsetsBottomHeight(WindowInsets.tappableElement))
}

@Composable
private fun SystemNavBarScrimContent(modifier: Modifier = Modifier) {
    val color = FloatingNavBarPalette.Default.scrim
    val fadeHeight = with(LocalDensity.current) { ScrimFadeHeight.toPx() }
    val brush = remember(color, fadeHeight) { scrimBrush(color, endY = fadeHeight) }
    Spacer(
        modifier
            .fillMaxWidth()
            .background(brush),
    )
}

private val ScrimFadeHeight = 16.dp

@Composable
@PreviewLightDark
@Suppress("MagicNumber")
private fun SystemNavBarScrimPreview() {
    MpeixTheme {
        Box(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .size(width = 360.dp, height = 160.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(4) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(
                                color = MpeixTheme.palette.primary,
                                shape = RoundedCornerShape(8.dp),
                            ),
                    )
                }
            }
            // Previews have no system bars, so the scrim gets the height of the 3-button bar
            SystemNavBarScrimContent(
                Modifier
                    .align(Alignment.BottomCenter)
                    .height(48.dp),
            )
        }
    }
}
