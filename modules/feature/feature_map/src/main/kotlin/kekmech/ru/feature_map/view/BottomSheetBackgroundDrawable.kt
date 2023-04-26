package kekmech.ru.feature_map.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable

@Suppress("TooManyFunctions")
internal class BottomSheetBackgroundDrawable(
    private val backgroundColor: Int,
    private val topCornerRadius: Float
) : Drawable() {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val boundsF: RectF = RectF()
    private val boundsI: Rect = Rect()

    private var tintFilter: PorterDuffColorFilter? = null
    private var tint: ColorStateList? = null
    private var tintMode: PorterDuff.Mode? = PorterDuff.Mode.SRC_IN

    var cornerRadiusFactor: Float = 1f
        set(value) {
            if (field == value) return
            field = value.coerceIn(0f..1f)
            cornerRadius = topCornerRadius * field
        }
    private var cornerRadius: Float = topCornerRadius
        set(value) {
            if (field == value) return
            field = value
            updateBounds(null)
            invalidateSelf()
        }
    private var animator: CornersAnimator? = null

    init {
        paint.color = backgroundColor
    }

    override fun draw(canvas: Canvas) {
        val clearColorFilter: Boolean
        if (tintFilter != null && paint.colorFilter == null) {
            paint.colorFilter = tintFilter
            clearColorFilter = true
        } else {
            clearColorFilter = false
        }

        canvas.drawRoundRect(boundsF, cornerRadius, cornerRadius, paint)
        canvas.drawRect(boundsF.left,boundsF.height() - cornerRadius, boundsF.right, boundsF.bottom, paint)

        if (clearColorFilter) paint.colorFilter = null
    }

    private fun updateBounds(boundsForUpdate: Rect?) {
        val bounds: Rect = boundsForUpdate ?: bounds
        boundsF.set(bounds.left, bounds.top, bounds.right, bounds.bottom)
        boundsI.set(bounds)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        updateBounds(bounds)
    }

    override fun getOutline(outline: Outline) = outline.setRoundRect(boundsI, cornerRadius)

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setTintList(tint: ColorStateList?) {
        this.tint = tint
        tintFilter = createTintFilter(this.tint, tintMode)
        invalidateSelf()
    }

    override fun setTintMode(tintMode: PorterDuff.Mode?) {
        this.tintMode = tintMode!!
        tintFilter = createTintFilter(tint, this.tintMode)
        invalidateSelf()
    }

    override fun onStateChange(stateSet: IntArray): Boolean {
        paint.color = backgroundColor
        if (tint != null && tintMode != null) {
            tintFilter = createTintFilter(tint, tintMode)
        }
        return true
    }

    private fun createTintFilter(
        tint: ColorStateList?,
        tintMode: PorterDuff.Mode?
    ): PorterDuffColorFilter? {
        if (tint == null || tintMode == null) {
            return null
        }
        val color = tint.getColorForState(state, Color.TRANSPARENT)
        return PorterDuffColorFilter(color, tintMode)
    }

    override fun isStateful() =
        (tint != null && tint!!.isStateful || super.isStateful())

    override fun getMinimumHeight() = (topCornerRadius * 2).toInt()

    override fun getMinimumWidth() = (topCornerRadius * 2).toInt()

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    private fun RectF.set(left: Int, top: Int, right: Int, bottom: Int) =
        set(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())

    fun animate(): CornersAnimator {
        animator?.cancel()
        return CornersAnimator()
    }

    inner class CornersAnimator {
        private var duration: Long = DEFAULT_CORNERS_ANIM_DURATION
        private var factor: Float = 1f
        private var animator: Animator? = null

        fun cancel() = animator?.cancel()

        fun setDuration(duration: Long): CornersAnimator {
            this.duration = duration
            return this
        }

        fun radius(factor: Float): CornersAnimator {
            this.factor = factor
            return this
        }

        fun start() {
            animator = ValueAnimator.ofFloat(cornerRadiusFactor, factor).apply {
                duration = this@CornersAnimator.duration
                addUpdateListener { cornerRadiusFactor = it.animatedValue as Float }
                start()
            }
        }
    }

    private companion object {
        private const val DEFAULT_CORNERS_ANIM_DURATION = 100L
    }
}
