package kekmech.ru.ext_android.views

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup

private const val DEFAULT_DEBOUNCE_MILLIS = 300L

fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    (layoutParams as ViewGroup.MarginLayoutParams).apply {
        left?.let { leftMargin = it }
        top?.let { topMargin = it }
        right?.let { rightMargin = it }
        bottom?.let { bottomMargin = it }
    }
}

fun View.setOnClickListenerWithDebounce(onClickListener: View.OnClickListener) =
    setOnClickListenerWithDebounce(DEFAULT_DEBOUNCE_MILLIS, onClickListener)

fun View.setOnClickListenerWithDebounce(debounceDuration: Long, onClickListener: View.OnClickListener) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickMillis = SystemClock.uptimeMillis()

        override fun onClick(v: View?) {
            val currentClickMillis = SystemClock.uptimeMillis()
            if (currentClickMillis - lastClickMillis >= debounceDuration) {
                lastClickMillis = currentClickMillis
                onClickListener.onClick(v)
            }
        }
    })
}


const val SCALE_ON_TOUCH_FACTOR = 0.97f
const val SCALE_ON_TOUCH_DURATION = 100L

@SuppressLint("ClickableViewAccessibility")
fun View.scaleOnTouch(
    factor: Float = SCALE_ON_TOUCH_FACTOR,
    duration: Long = SCALE_ON_TOUCH_DURATION,
) {
    setOnTouchListener { _, event ->
        if (event.action == ACTION_DOWN) {
            clearAnimation()
            animate()
                .scaleX(factor)
                .scaleY(factor)
                .setDuration(duration)
                .start()
        } else {
            clearAnimation()
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(duration)
                .start()
        }
        false
    }
}
