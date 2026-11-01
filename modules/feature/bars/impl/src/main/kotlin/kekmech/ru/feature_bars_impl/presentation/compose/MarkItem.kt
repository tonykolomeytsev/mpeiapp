package kekmech.ru.feature_bars_impl.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.floor

/**
 * Colored chip with the [mark]: green for good marks, yellow for satisfactory and red for bad ones.
 */
@Composable
internal fun MarkItem(
    mark: Float,
    modifier: Modifier = Modifier,
    text: String = mark.formatMark(),
) {
    val (textColor, backgroundColor) = mark.markColors()
    Text(
        text = text,
        style = MpeixTheme.typography.bodyLarge,
        color = textColor,
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

private fun Float.formatMark(): String =
    if (floor(this) == this) toInt().toString() else toString()

@Suppress("MagicNumber")
@Composable
private fun Float.markColors(): Pair<Color, Color> =
    with(MpeixTheme.palette) {
        when (this@markColors) {
            in 3.5f..5f -> onMarkGreen to markGreen
            in 2.5f..<3.5f -> onMarkYellow to markYellow
            in 0f..<2.5f -> onMarkRed to markRed
            else -> onSurfaceVariant to surfaceContainerHighest
        }
    }

@PreviewLightDark
@Composable
private fun MarkItemPreview() {
    MpeixTheme {
        Row(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            MarkItem(mark = 5f)
            MarkItem(mark = 4.5f)
            MarkItem(mark = 3f)
            MarkItem(mark = 2f)
            MarkItem(mark = 0f)
            MarkItem(mark = -1f, text = "–")
        }
    }
}
