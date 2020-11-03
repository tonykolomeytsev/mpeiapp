package kekmech.ru.common_android.views

import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

const val DEFAULT_MILLIS_PER_INCH = 400f

fun RecyclerView.smoothScrollToPosition(position: Int, millisPerInch: Float = DEFAULT_MILLIS_PER_INCH) {
    layoutManager?.let {
        val linearSmoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return millisPerInch / displayMetrics.densityDpi
            }
        }
        linearSmoothScroller.targetPosition = position
        it.startSmoothScroll(linearSmoothScroller)
    }
}