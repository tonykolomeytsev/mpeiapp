package kekmech.ru.ui_shimmer


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.OnGloballyPositionedModifier
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo

fun Modifier.shimmer(): Modifier = composed(
    factory = {
        val bounds = rememberShimmerBounds()
        val shimmerWidthPx = with(LocalDensity.current) { ShimmerTokens.Width.toPx() }
        val effect = remember(shimmerWidthPx) { ShimmerEffect(shimmerWidthPx) }
        val area = remember(shimmerWidthPx, bounds) { ShimmerArea(shimmerWidthPx, bounds) }

        LaunchedEffect(area) {
            effect.startAnimation()
        }

        remember(area) { ShimmerModifier(area, effect) }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shimmer"
    }
)

internal class ShimmerModifier(
    private val area: ShimmerArea,
    private val effect: ShimmerEffect,
) : DrawModifier, OnGloballyPositionedModifier {

    override fun ContentDrawScope.draw() {
        with(effect) { draw(area) }
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        val viewBounds = coordinates.asRect()
        area.viewBounds = viewBounds
    }

    private fun LayoutCoordinates.asRect(): Rect {
        val positionInWindow = positionInWindow()
        return Rect(
            left = positionInWindow.x,
            top = positionInWindow.y,
            right = positionInWindow.x + size.width,
            bottom = positionInWindow.y + size.height,
        )
    }
}
