package kekmech.ru.lib_snowfall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import java.util.Random
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private const val DEFAULT_FLAKES_COUNT = 100

// Flake look, same as the old SnowfallView setup. Bigger flakes are closer, so they fall faster
private const val MIN_SIZE_DP = 2f
private const val MAX_SIZE_DP = 8f
private const val MIN_SPEED_DP_PER_SECOND = 80f
private const val MAX_SPEED_DP_PER_SECOND = 480f
private const val MAX_ANGLE_DEGREES = 10f
private const val MIN_ALPHA = 150f / 255f
private const val MAX_ALPHA = 250f / 255f

// A long frame slows the snow down instead of making it jump
private const val MAX_STEP_SECONDS = 0.05f
private const val NANOS_IN_SECOND = 1_000_000_000f
private const val NO_FRAME = Long.MIN_VALUE

@Composable
public fun rememberSnowfallState(flakesCount: Int = DEFAULT_FLAKES_COUNT): SnowfallState =
    remember(flakesCount) { SnowfallState(flakesCount) }

/**
 * Flakes drawn by [Snowfall].
 *
 * Remember it above the screens which show the snow: the next [Snowfall] drawing the same state
 * continues from where the previous one stopped instead of starting from the top again.
 */
@Stable
public class SnowfallState(
    private val flakesCount: Int = DEFAULT_FLAKES_COUNT,
) {

    private val random = Random()

    // Arrays instead of flake objects: nothing is allocated per frame. Units are px and px/s
    private val x = FloatArray(flakesCount)
    private val y = FloatArray(flakesCount)
    private val speedX = FloatArray(flakesCount)
    private val speedY = FloatArray(flakesCount)
    private val radius = FloatArray(flakesCount)
    private val alpha = FloatArray(flakesCount)

    private var bounds = Size.Zero
    private var density = 1f
    private var lastFrameNanos = NO_FRAME
    private var runningAnimations = 0

    // Only drawing reads it, so every step redraws the snow without recomposition
    private var frameNanos by mutableLongStateOf(NO_FRAME)

    internal suspend fun animate() {
        // Time spent off screen doesn't count: the snow continues from where it stopped
        if (runningAnimations++ == 0) lastFrameNanos = NO_FRAME
        try {
            while (true) withFrameNanos(::step)
        } finally {
            runningAnimations--
        }
    }

    private fun step(frameNanos: Long) {
        val previousFrameNanos = lastFrameNanos
        // Two Snowfalls may share the state for a moment (e.g. while tabs switch), the second one skips the frame
        if (frameNanos <= previousFrameNanos) return
        lastFrameNanos = frameNanos
        if (previousFrameNanos == NO_FRAME || bounds.isEmpty()) return

        val seconds = ((frameNanos - previousFrameNanos) / NANOS_IN_SECOND).coerceAtMost(MAX_STEP_SECONDS)
        val height = bounds.height
        for (i in 0 until flakesCount) {
            x[i] += speedX[i] * seconds
            y[i] += speedY[i] * seconds
            if (y[i] > height) spawn(i, spreadAboveTop = false)
        }
        this.frameNanos = frameNanos
    }

    internal fun DrawScope.drawSnow(color: Color) {
        updateBounds(size, density)
        // Before the first step all flakes are above the top edge anyway
        if (frameNanos == NO_FRAME) return

        val height = bounds.height
        for (i in 0 until flakesCount) {
            val r = radius[i]
            if (y[i] + r < 0f) continue
            // Flakes melt away towards the bottom edge
            val fade = ((height - y[i]) / height).coerceIn(0f, 1f)
            drawCircle(color = color, radius = r, center = Offset(x[i], y[i]), alpha = alpha[i] * fade)
        }
    }

    private fun updateBounds(size: Size, density: Float) {
        if (size.isEmpty() || size == bounds) return
        if (bounds.isEmpty()) {
            bounds = size
            this.density = density
            for (i in 0 until flakesCount) spawn(i, spreadAboveTop = true)
        } else {
            // Keep the flakes in place relative to the new size, e.g. after rotation
            val scaleX = size.width / bounds.width
            val scaleY = size.height / bounds.height
            for (i in 0 until flakesCount) {
                x[i] *= scaleX
                y[i] *= scaleY
            }
            bounds = size
        }
    }

    /**
     * Gives the flake a new look and puts it above the top edge. The first flakes are spread over
     * a screen height above it, so the snow doesn't come down as a single wall.
     */
    private fun spawn(i: Int, spreadAboveTop: Boolean) {
        // Most flakes are small and slow, big fast ones are rare
        val sizeFraction = nextGaussianFraction()
        val size = lerp(MIN_SIZE_DP, MAX_SIZE_DP, sizeFraction) * density
        val speed = lerp(MIN_SPEED_DP_PER_SECOND, MAX_SPEED_DP_PER_SECOND, sizeFraction) * density
        val angle = Math.toRadians(random.nextFloat() * MAX_ANGLE_DEGREES * nextSign().toDouble())

        radius[i] = size / 2
        speedX[i] = speed * sin(angle).toFloat()
        speedY[i] = speed * cos(angle).toFloat()
        alpha[i] = lerp(MIN_ALPHA, MAX_ALPHA, random.nextFloat())
        x[i] = random.nextFloat() * bounds.width
        y[i] = if (spreadAboveTop) {
            random.nextFloat() * bounds.height - bounds.height - radius[i]
        } else {
            -radius[i]
        }
    }

    private fun nextGaussianFraction(): Float {
        // 3 sigmas cover more than 99% of values, the rest are rolled again
        var value: Double
        do {
            value = random.nextGaussian() / 3
        } while (value <= -1 || value >= 1)
        return abs(value).toFloat()
    }

    private fun nextSign(): Int = if (random.nextBoolean()) 1 else -1

    private fun lerp(start: Float, stop: Float, fraction: Float): Float = start + (stop - start) * fraction
}
