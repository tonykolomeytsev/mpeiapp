package kekmech.ru.ui_shimmer

import android.graphics.Matrix
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer

internal class ShimmerEffect(
    shimmerWidthPx: Float,
) {

    private val animatedState = Animatable(0f)

    private val transformationMatrix = Matrix()

    private val shader = LinearGradientShader(
        from = Offset(-shimmerWidthPx / 2, 0f),
        to = Offset(shimmerWidthPx / 2, 0f),
        colors = listOf(
            Color.Unspecified.copy(alpha = 0.5f),
            Color.Unspecified.copy(alpha = 1.0f),
            Color.Unspecified.copy(alpha = 0.5f),
        ),
        colorStops = listOf(
            0.0f,
            0.5f,
            1.0f,
        ),
    )

    private val paint = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Fill
        blendMode = BlendMode.DstIn
        shader = this@ShimmerEffect.shader
    }

    internal suspend fun startAnimation() {
        animatedState.animateTo(
            targetValue = 1f,
            animationSpec = ShimmerTokens.AnimationSpec,
        )
    }

    private val emptyPaint = Paint()

    fun ContentDrawScope.draw(shimmerArea: ShimmerArea) = with(shimmerArea) {
        if (shimmerBounds.isEmpty || viewBounds.isEmpty) return

        val progress = animatedState.value
        val traversal = -translationDistance / 2 + translationDistance * progress + pivotPoint.x

        transformationMatrix.apply {
            reset()
            postTranslate(traversal, 0f)
            postRotate(ShimmerTokens.Rotation, pivotPoint.x, pivotPoint.y)
        }
        shader.setLocalMatrix(transformationMatrix)

        val drawArea = size.toRect()
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(
                bounds = drawArea,
                paint = emptyPaint
            ) {
                drawContent()
                canvas.drawRect(drawArea, paint)
            }
        }
    }
}
