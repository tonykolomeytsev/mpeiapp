package kekmech.ru.coreui.slider

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.math.abs
import kotlin.math.min
import androidx.recyclerview.widget.LinearSmoothScroller

class EnumSliderLayoutManager(
    private val context: Context,
    private val recyclerWidth: Int,
    private val childWidth: Int
) : LinearLayoutManager(context, HORIZONTAL, false) {

    private val mShrinkAmount = 0.65f
    private val mShrinkDistance = 0.9f

    private var lastSelectedPosition: Int = 0
    var selectedPosition: Int
        get() = lastSelectedPosition
        set(value) {
            scrollToPosition(value)
            initFirstState()
            lastSelectedPosition = value
        }

    override fun canScrollHorizontally() = true

    override fun canScrollVertically() = false

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val scrolled = super.scrollHorizontallyBy(dx, recycler, state)

        val midpoint = width / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount
        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            if (child.x in (recyclerWidth / 2 - (3f*childWidth))..recyclerWidth / 2 + (2f*childWidth) ) {
                val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
                val d = min(d1, abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.scaleX = scale
                child.scaleY = scale
                child.alpha = scale*scale
            } else {
                child.alpha = 0f
            }
        }
        return scrolled
    }

    fun onTouchRelease(
        recyclerView: RecyclerView,
        state: RecyclerView.State?
    ) {
        var maxAlphaIndex = 0
        var maxAlpha = 0f
        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            val alpha = child.alpha
            if (alpha > maxAlpha) {
                maxAlpha = alpha
                maxAlphaIndex = recyclerView.getChildAdapterPosition(child)
            }
        }
        lastSelectedPosition = maxAlphaIndex
        postOnAnimation { smoothScrollToPosition(recyclerView, state, maxAlphaIndex) }
    }

    fun initFirstState() {
        val midpoint = width / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount
        for (i in 0 until childCount) {
            val child = getChildAt(i)!!
            if (child.x in (recyclerWidth / 2 - (3f*childWidth))..recyclerWidth / 2 + (2f*childWidth) ) {
                val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
                val d = min(d1, abs(midpoint - childMidpoint))
                val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                child.scaleX = scale
                child.scaleY = scale
                child.alpha = scale*scale
            } else {
                child.alpha = 0f
            }
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State?, position: Int) {
        val centerSmoothScroller = CenterSmoothScroller(recyclerView.context)
        centerSmoothScroller.targetPosition = position
        startSmoothScroll(centerSmoothScroller)
        lastSelectedPosition = position
    }

    private class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        private val smoothness = 0.5f // the smaller the smoother

        override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int =
            (((boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)) * smoothness).toInt()
    }
}