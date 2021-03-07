package kekmech.ru.common_android.views

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.HapticFeedbackConstants
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

@SuppressLint("ClickableViewAccessibility")
fun View.enableHapticFeedback() {
    isHapticFeedbackEnabled = true
    setOnTouchListener { _, event ->
        if (event.action == ACTION_DOWN) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
        false
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