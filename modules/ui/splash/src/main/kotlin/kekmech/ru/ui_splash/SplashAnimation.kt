package kekmech.ru.ui_splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.clipPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

// Timeline (ms). One linear clock drives every phase, so all layers stay in sync at any frame rate.
internal const val TIMELINE_MS = 2300
internal const val FADE_OUT_MS = 260

private const val PLATE_START = 0
private const val PLATE_END = 450
private const val TRACE_START = 250
private const val TRACE_END = 1250
private const val TRACE_FADE_END = 1550
private const val REVEAL_START = 700
private const val REVEAL_END = 1500
private const val HALO_START = 900
private const val HALO_END = 1500
private const val PULSE_1_START = 1350
private const val PULSE_1_END = 2000
private const val PULSE_2_START = 1600
private const val PULSE_2_END = 2250
private const val TEXT_START = 1100
private const val TEXT_END = 1700
private const val HOLD_WITHOUT_ANIMATION_MS = 400L

private val MaxEmblemRadius = 110.dp
private const val EMBLEM_CENTER_Y = 0.45f

private val PlateColor = Color(0xFF0D2352)
private val GoldColor = Color(0xFFE8C766)
private val GoldHeadColor = Color(0xFFFFF3C4)
private val AccentColor = Color(0xFF3D9BFF)
private val BrandTextColor = Color(0xFFB9C3D8)
private val EaseInOut = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)

/**
 * Splash animation: dark plate fades in, a golden light runs around the rim of the emblem,
 * the emblem is revealed from its edge towards the centre, two soft glow pulses spread out
 * from the centre while the brand name slides up from the bottom.
 *
 * @param animate false when the user disabled animations system-wide: the final frame is shown at once
 */
@Composable
internal fun SplashAnimation(
    logo: Painter,
    brandName: String,
    animate: Boolean,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 0..1 over TIMELINE_MS. Only read inside draw / graphicsLayer lambdas -> no recomposition per frame
    val timeline = remember { Animatable(if (animate) 0f else 1f) }
    val exitAlpha = remember { Animatable(1f) }

    LaunchedEffect(animate) {
        if (animate) {
            timeline.animateTo(1f, tween(durationMillis = TIMELINE_MS, easing = LinearEasing))
        } else {
            delay(HOLD_WITHOUT_ANIMATION_MS)
        }
        exitAlpha.animateTo(0f, tween(durationMillis = FADE_OUT_MS, easing = LinearEasing))
        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer { alpha = exitAlpha.value },
    ) {
        val path = remember { Path() }
        val clipRing = remember { Path() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val t = timeline.value
            drawEmblem(
                logo = logo,
                t = t,
                clipRing = clipRing,
            )
            drawRim(t = t, ringPath = path)
            drawPulse(t = t, startMs = PULSE_1_START, endMs = PULSE_1_END)
            drawPulse(t = t, startMs = PULSE_2_START, endMs = PULSE_2_END)
        }

        BasicText(
            text = brandName,
            style = TextStyle(
                color = BrandTextColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 56.dp)
                .graphicsLayer {
                    val p = phase(timeline.value, TEXT_START, TEXT_END, FastOutSlowInEasing)
                    alpha = p
                    translationY = (1f - p) * 18.dp.toPx()
                },
        )
    }
}

private fun DrawScope.emblemRadius(): Float = min(size.minDimension * 0.24f, MaxEmblemRadius.toPx())

private fun DrawScope.emblemCenter(): Offset = Offset(size.width / 2f, size.height * EMBLEM_CENTER_Y)

/** Plate, soft halo and the emblem itself, revealed by a shrinking hole (edge -> centre). */
private fun DrawScope.drawEmblem(logo: Painter, t: Float, clipRing: Path) {
    val radius = emblemRadius()
    val center = emblemCenter()

    val plate = phase(t, PLATE_START, PLATE_END, FastOutSlowInEasing)
    val reveal = phase(t, REVEAL_START, REVEAL_END, FastOutSlowInEasing)
    val halo = phase(t, HALO_START, HALO_END, FastOutSlowInEasing)

    // Ambient halo behind the emblem
    if (halo > 0f) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(AccentColor.copy(alpha = 0.32f * halo), Color.Transparent),
                center = center,
                radius = radius * 1.7f,
            ),
            radius = radius * 1.7f,
            center = center,
        )
    }

    // Dark plate fading in with a tiny scale-up
    if (plate > 0f) {
        drawCircle(
            color = PlateColor.copy(alpha = plate),
            radius = radius * (0.92f + 0.08f * plate),
            center = center,
        )
    }

    // Emblem: visible area is a ring between `hole` and the outer edge, hole shrinks to zero
    if (reveal > 0f) {
        val hole = radius * (1f - reveal)
        clipRing.rewind()
        clipRing.fillType = PathFillType.EvenOdd
        clipRing.addOval(Rect(center, radius * 1.02f))
        if (hole > 0.5f) clipRing.addOval(Rect(center, hole))
        clipPath(clipRing) {
            translate(left = center.x - radius, top = center.y - radius) {
                with(logo) { draw(size = Size(radius * 2f, radius * 2f), alpha = reveal.coerceAtLeast(0.15f)) }
            }
        }
    }
}

/** Golden light running clockwise around the rim, with a comet tail and a glowing head. */
private fun DrawScope.drawRim(t: Float, ringPath: Path) {
    val progress = phase(t, TRACE_START, TRACE_END, EaseInOut)
    val fade = 1f - phase(t, TRACE_END, TRACE_FADE_END, LinearEasing)
    if (progress <= 0f || fade <= 0f) return

    val radius = emblemRadius()
    val center = emblemCenter()
    val rimRadius = radius * 0.955f
    val strokeWidth = max(2.dp.toPx(), radius * 0.028f)
    val headDegrees = -90f + 360f * progress

    // The part of the rim already travelled stays softly lit
    ringPath.rewind()
    ringPath.addArc(
        oval = Rect(center, rimRadius),
        startAngleDegrees = -90f,
        sweepAngleDegrees = 360f * progress,
    )
    drawPath(
        path = ringPath,
        color = GoldColor.copy(alpha = 0.35f * fade),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )

    // Comet tail: a sweep gradient rotated so that its bright end sits at the head
    val tail = 0.22f
    rotate(degrees = headDegrees, pivot = center) {
        drawCircle(
            brush = Brush.sweepGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    (1f - tail) to Color.Transparent,
                    1f to GoldHeadColor.copy(alpha = fade),
                ),
                center = center,
            ),
            radius = rimRadius,
            center = center,
            style = Stroke(width = strokeWidth * 1.8f, cap = StrokeCap.Round),
        )
    }

    // Head glow
    val rad = Math.toRadians(headDegrees.toDouble())
    val head = Offset(
        x = center.x + rimRadius * cos(rad).toFloat(),
        y = center.y + rimRadius * sin(rad).toFloat(),
    )
    val glowRadius = strokeWidth * 5f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(GoldHeadColor.copy(alpha = 0.9f * fade), GoldColor.copy(alpha = 0.35f * fade), Color.Transparent),
            center = head,
            radius = glowRadius,
        ),
        radius = glowRadius,
        center = head,
    )
}

/** Soft ring of light expanding from the emblem centre. */
private fun DrawScope.drawPulse(t: Float, startMs: Int, endMs: Int) {
    val ms = t * TIMELINE_MS
    if (ms < startMs || ms > endMs) return
    val p = phase(t, startMs, endMs, FastOutSlowInEasing)

    val radius = emblemRadius()
    val center = emblemCenter()
    val maxRadius = radius * 1.9f
    val ringCenter = (0.3f + 0.6f * p).coerceIn(0.2f, 0.88f)
    val thickness = 0.12f
    val alpha = (1f - p) * 0.55f

    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                (ringCenter - thickness) to Color.Transparent,
                ringCenter to AccentColor.copy(alpha = alpha),
                (ringCenter + thickness) to Color.Transparent,
                1f to Color.Transparent,
            ),
            center = center,
            radius = maxRadius,
        ),
        radius = maxRadius,
        center = center,
        blendMode = BlendMode.Plus,
    )
}

/** Progress (0..1, eased) of a phase that lasts from [startMs] to [endMs] of the [TIMELINE_MS] clock. */
internal fun phase(t: Float, startMs: Int, endMs: Int, easing: Easing): Float {
    val ms = t * TIMELINE_MS
    val linear = ((ms - startMs) / (endMs - startMs).toFloat()).coerceIn(0f, 1f)
    return easing.transform(linear)
}
