package kekmech.ru.common_android.views

import android.view.HapticFeedbackConstants
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes

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

fun View.enableHapticFeedback() {
    isHapticFeedbackEnabled = true
    setOnTouchListener { _, event ->
        if (event.action == ACTION_DOWN) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
        false
    }
}