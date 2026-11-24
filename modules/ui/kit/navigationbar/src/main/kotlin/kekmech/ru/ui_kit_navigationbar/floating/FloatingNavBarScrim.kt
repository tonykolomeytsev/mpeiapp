package kekmech.ru.ui_kit_navigationbar.floating

import androidx.compose.animation.core.EaseInOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # FloatingNavBarScrim
 *
 * Gradient scrim drawn under the [FloatingNavBar], like in iOS or Telegram: the content
 * scrolled under the navigation bar smoothly fades into the surface color. The scrim is
 * transparent at the top and opaque at the bottom.
 *
 * Place the scrim together with the navigation bar, so it stretches from the top of the bar
 * to the bottom edge of the screen:
 * ```kotlin
 * Box(
 *     modifier = Modifier
 *         .align(Alignment.BottomCenter)
 *         .fillMaxWidth(),
 * ) {
 *     FloatingNavBarScrim(Modifier.matchParentSize())
 *     FloatingNavBar(
 *         modifier = Modifier
 *             .navigationBarsPadding()
 *             .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
 *     ) {
 *         // tabs ...
 *     }
 * }
 * ```
 *
 * The scrim doesn't consume touches, so the content under it stays clickable.
 *
 * @param modifier the [Modifier] to be applied to this scrim
 * @param palette colors of the navigation bar
 */
@Composable
public fun FloatingNavBarScrim(
    modifier: Modifier = Modifier,
    palette: FloatingNavBarPalette = FloatingNavBarPalette.Default,
) {
    val color = palette.scrim
    val brush = remember(color) { scrimBrush(color) }
    Spacer(modifier.background(brush))
}

/**
 * Linear gradient has a visible edge where it starts, so the alpha follows an ease-in-out curve.
 * The transparent end keeps the scrim color to avoid gray tints in the middle of the gradient.
 *
 * By default the gradient spans the whole height. With [endY] it ends at that offset in pixels,
 * and the scrim stays opaque below it.
 */
internal fun scrimBrush(color: Color, endY: Float = Float.POSITIVE_INFINITY): Brush {
    val colorStops = Array(ScrimGradientSteps + 1) { step ->
        val fraction = step.toFloat() / ScrimGradientSteps
        fraction to color.copy(alpha = color.alpha * EaseInOut.transform(fraction))
    }
    return Brush.verticalGradient(colorStops = colorStops, endY = endY)
}

private const val ScrimGradientSteps = 8

@Composable
@PreviewLightDark
@Suppress("MagicNumber")
private fun FloatingNavBarScrimPreview() {
    MpeixTheme {
        Box(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .size(width = 360.dp, height = 240.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                repeat(6) {
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
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            ) {
                FloatingNavBarScrim(Modifier.matchParentSize())
                FloatingNavBar(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                ) {
                    Tab(
                        selected = true,
                        onClick = {},
                        iconSelected = Icons.Filled.Home,
                        iconUnselected = Icons.Outlined.Home,
                        label = "Главная",
                    )
                    Tab(
                        selected = false,
                        onClick = {},
                        iconSelected = Icons.Filled.DateRange,
                        iconUnselected = Icons.Outlined.DateRange,
                        label = "Расписание",
                    )
                    Tab(
                        selected = false,
                        onClick = {},
                        iconSelected = Icons.Filled.LocationOn,
                        iconUnselected = Icons.Outlined.LocationOn,
                        label = "Карта",
                    )
                    Tab(
                        selected = false,
                        onClick = {},
                        iconSelected = Icons.Filled.AccountCircle,
                        iconUnselected = Icons.Outlined.AccountCircle,
                        label = "Профиль",
                    )
                }
            }
        }
    }
}
