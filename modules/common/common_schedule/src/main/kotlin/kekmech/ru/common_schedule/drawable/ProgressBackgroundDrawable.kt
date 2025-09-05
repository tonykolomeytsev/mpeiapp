package kekmech.ru.common_schedule.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import kekmech.ru.common_android.dpToPx

public class ProgressBackgroundDrawable(
    context: Context,
    @ColorInt private val backgroundColor: Int,
    @ColorInt private val progressColor: Int,
    private val cornerRadius: CornerRadius
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val boundsF: RectF = RectF()
    private val path: Path = Path()
    public var progress: Float = 0f
        set(value) {
            field = value
            invalidateSelf()
        }
    private val strokeWidth = context.resources.dpToPx(PROGRESS_STROKE_WIDTH).toFloat()
    private val horizontalOffset = context.resources.dpToPx(HORIZONTAL_OFFSET).toFloat()

    override fun draw(canvas: Canvas) {
        if (!path.isEmpty) {
            canvas.clipPath(path)
        }
        paint.alpha = BACKGROUND_ALPHA
        paint.color = backgroundColor
        canvas.drawRect(boundsF, paint)

        // progress starts 16 dp from the left and ends 16 dp from the right
        val progressRight = horizontalOffset + ((boundsF.right - horizontalOffset * 2) * progress)
        // draw progress fill
        paint.color = progressColor
        paint.alpha = PROGRESS_BACKGROUND_ALPHA
        canvas.drawRect(0f, 0f, progressRight, boundsF.bottom, paint)
        // draw right stroke
        paint.alpha = PROGRESS_STROKE_ALPHA
        canvas.drawRect(progressRight - strokeWidth, 0f, progressRight, boundsF.bottom, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?): Unit = Unit

    private fun updateBounds(boundsForUpdate: Rect?) {
        val bounds: Rect = boundsForUpdate ?: bounds
        boundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom)
        path.rewind()
        path.addRoundRect(boundsF, cornerRadius.toRadFloatArray(), Path.Direction.CW)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        updateBounds(bounds)
    }

    private fun RectF.set(left: Int, top: Int, right: Int, bottom: Int) =
        set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    public data class CornerRadius(
        @Dimension val topLeft: Float,
        @Dimension val topRight: Float,
        @Dimension val bottomRight: Float,
        @Dimension val bottomLeft: Float
    ) {

        public fun toRadFloatArray(): FloatArray = floatArrayOf(
            topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft
        )

        public companion object {
            public fun of(@Dimension radius: Float): CornerRadius = CornerRadius(radius, radius, radius, radius)
        }
    }

    private companion object {
        const val BACKGROUND_ALPHA = 255
        const val PROGRESS_BACKGROUND_ALPHA = 25
        const val PROGRESS_STROKE_ALPHA = 38
        const val PROGRESS_STROKE_WIDTH = 4
        const val HORIZONTAL_OFFSET = 16
    }
}
