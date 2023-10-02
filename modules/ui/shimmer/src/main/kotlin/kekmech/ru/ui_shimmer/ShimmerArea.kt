package kekmech.ru.ui_shimmer

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Describes the area in which the shimmer effect will be drawn.
 */
internal class ShimmerArea(
    private val shimmerWidthPx: Float,
    internal val shimmerBounds: Rect,
) {

    private val reducedRotation = ShimmerTokens.Rotation.toRadian()

    private var shimmerSize: Size = Size.Zero

    var translationDistance = 0f
        private set

    var pivotPoint = Offset.Unspecified
        private set

    var viewBounds = Rect.Zero
        set(value) {
            if (value == field) return
            field = value
            computeShimmerBounds()
        }

    private fun computeShimmerBounds() {
        if (viewBounds.isEmpty) return

        // Pivot point in the view's frame of reference
        pivotPoint = -viewBounds.topLeft + shimmerBounds.center

        val newShimmerSize = shimmerBounds.size
        if (shimmerSize != newShimmerSize) {
            shimmerSize = newShimmerSize
            computeTranslationDistance()
        }
    }

    /**
     * Rotating the shimmer results in an effect that will first be visible in one of the corners.
     * It will afterwards travel across the view / display until the last visible part of it will
     * disappear in the opposite corner.
     *
     * A simple shimmer going across the device's screen from left to right has to travel until
     * it reaches the center of the screen and then the same distance again. Without taking the
     * shimmer's own width into account.
     *
     * If the shimmer is now tilted slightly clockwise around the center of the display, a new
     * distance has to be calculated. The required distance is the length of a line, which extends
     * from the top left of the display to the rotated shimmer (or center line), hitting it at a
     * 90 degree angle. As the height and width of the display (or view) are known, the length of
     * the line can be calculated by using basic trigonometric functions.
     */
    private fun computeTranslationDistance() {
        val width = shimmerSize.width / 2
        val height = shimmerSize.height / 2

        val distanceCornerToCenter = sqrt(width.pow(2) + height.pow(2))
        val beta = acos(width / distanceCornerToCenter)
        val alpha = beta - reducedRotation

        val distanceCornerToRotatedCenterLine = cos(alpha) * distanceCornerToCenter
        translationDistance = distanceCornerToRotatedCenterLine * 2 + shimmerWidthPx
    }

    @Suppress("MagicNumber")
    private fun Float.toRadian(): Float = this / 180 * Math.PI.toFloat()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShimmerArea

        if (shimmerWidthPx != other.shimmerWidthPx) return false
        if (reducedRotation != other.reducedRotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shimmerWidthPx.hashCode()
        result = 31 * result + reducedRotation.hashCode()
        return result
    }
}
