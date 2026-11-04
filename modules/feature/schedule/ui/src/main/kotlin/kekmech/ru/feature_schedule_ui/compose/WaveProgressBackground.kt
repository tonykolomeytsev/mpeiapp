package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlin.math.PI
import kotlin.math.sin

private const val FILL_ALPHA = 0.1f
private const val EDGE_ALPHA = 0.15f
private const val WAVE_PERIOD_MILLIS = 2400
private val HorizontalOffset = 16.dp
private val EdgeWidth = 4.dp
private val WaveAmplitude = 2.5.dp
private val WaveLength = 64.dp
private val WaveStep = 2.dp

/**
 * Background of the ongoing classes, filled from the left edge up to the [progress].
 * The right edge of the fill is a vertical wave, slowly running from top to bottom.
 *
 * Like the old `ProgressBackgroundDrawable`, the fill starts [HorizontalOffset] from the left
 * and ends [HorizontalOffset] from the right, so that 0% and 100% are still visible.
 *
 * Wave phase is read only in the draw phase, so the animation doesn't cause recompositions.
 *
 * @param progress progress of the classes, from 0 to 1
 * @param color color of the fill, applied with transparency
 */
@Composable
internal fun WaveProgressBackground(
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        label = "WaveProgress",
    )
    val phase by rememberInfiniteTransition(label = "WavePhase").animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(WAVE_PERIOD_MILLIS, easing = LinearEasing)),
        label = "WavePhase",
    )
    val edge = remember { Path() }
    val fill = remember { Path() }

    Canvas(modifier) {
        val edgeX = HorizontalOffset.toPx() + (size.width - HorizontalOffset.toPx() * 2) * animatedProgress
        buildWave(edge, edgeX, phase)

        fill.rewind()
        fill.addPath(edge)
        fill.lineTo(0f, size.height)
        fill.lineTo(0f, 0f)
        fill.close()
        drawPath(fill, color.copy(alpha = FILL_ALPHA))

        // Edge is drawn inside the fill, like the stroke of the old drawable
        val edgeWidth = EdgeWidth.toPx()
        translate(left = -edgeWidth / 2) {
            drawPath(edge, color.copy(alpha = EDGE_ALPHA), style = Stroke(width = edgeWidth))
        }
    }
}

/**
 * Builds vertical sine wave around [x], from the top to the bottom of the canvas.
 */
private fun DrawScope.buildWave(path: Path, x: Float, phase: Float) {
    val amplitude = WaveAmplitude.toPx()
    val k = (2 * PI / WaveLength.toPx()).toFloat()
    val step = WaveStep.toPx()
    path.rewind()
    var y = 0f
    path.moveTo(x + amplitude * sin(-phase), y)
    while (y < size.height) {
        y = (y + step).coerceAtMost(size.height)
        // minus phase makes the wave run downwards
        path.lineTo(x + amplitude * sin(k * y - phase), y)
    }
}

@PreviewLightDark
@Composable
private fun WaveProgressBackgroundPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf(0f, 0.25f, 0.5f, 1f).forEach { progress ->
                WaveProgressBackground(
                    progress = progress,
                    color = MpeixTheme.palette.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MpeixTheme.palette.secondary),
                )
            }
        }
    }
}
