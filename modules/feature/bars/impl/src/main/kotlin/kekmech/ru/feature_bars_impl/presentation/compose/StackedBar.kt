package kekmech.ru.feature_bars_impl.presentation.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastForEach
import kekmech.ru.ui_theme.theme.MpeixTheme

/** One colored part of the [StackedBar]. */
internal data class StackPart(
    val value: Int,
    val color: Color,
)

/**
 * Horizontal pill-shaped bar split into [parts] proportionally to their values, with [spacing]
 * between neighboring parts. The height comes from [modifier].
 *
 * Parts with zero or negative values are skipped, so they don't leave empty gaps, and a negative
 * [spacing] is treated as 0. When no part has a positive value, the whole bar is filled with
 * [trackColor]; pass [Color.Transparent] to draw nothing instead.
 */
@Composable
internal fun StackedBar(
    parts: List<StackPart>,
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp,
    trackColor: Color = MpeixTheme.palette.surfaceContainerHighest,
) {
    val visibleParts = parts.fastFilter { it.value > 0 }
    // Summed as Long, so a few huge values can't overflow into a negative total
    val total = visibleParts.sumOf { it.value.toLong() }.toFloat()
    Canvas(
        modifier = modifier.clip(CircleShape),
    ) {
        if (total == 0f) {
            drawRect(color = trackColor)
            return@Canvas
        }
        val spacingPx = spacing.toPx().coerceAtLeast(0f)
        val availableWidth = (size.width - spacingPx * visibleParts.lastIndex).coerceAtLeast(0f)
        var x = 0f
        visibleParts.fastForEach { part ->
            val partWidth = availableWidth * part.value / total
            drawRect(
                color = part.color,
                topLeft = Offset(x, 0f),
                size = Size(partWidth, size.height),
            )
            x += partWidth + spacingPx
        }
    }
}

@Composable
@PreviewLightDark
private fun StackedBarPreview() {
    MpeixTheme {
        val green = MpeixTheme.palette.markGreen
        val yellow = MpeixTheme.palette.markYellow
        val red = MpeixTheme.palette.markRed
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(
                listOf(
                    StackPart(value = 175, color = green),
                    StackPart(value = 13, color = yellow),
                    StackPart(value = 4, color = red),
                ),
                listOf(
                    StackPart(value = 158, color = green),
                    StackPart(value = 15, color = yellow),
                    StackPart(value = 19, color = red),
                ),
                listOf(
                    StackPart(value = 192, color = green),
                    StackPart(value = 0, color = yellow),
                    StackPart(value = 0, color = red),
                ),
                listOf(
                    StackPart(value = 1, color = green),
                    StackPart(value = 1, color = yellow),
                    StackPart(value = 1, color = red),
                ),
                // Negative part is skipped
                listOf(
                    StackPart(value = 180, color = green),
                    StackPart(value = -12, color = yellow),
                    StackPart(value = 12, color = red),
                ),
                // Nothing positive: only the track
                listOf(
                    StackPart(value = 0, color = green),
                    StackPart(value = -3, color = red),
                ),
            ).fastForEach { parts ->
                StackedBar(
                    parts = parts,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                )
            }
        }
    }
}
