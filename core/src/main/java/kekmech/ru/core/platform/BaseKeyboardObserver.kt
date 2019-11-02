package kekmech.ru.core.platform

import android.graphics.Rect
import android.view.ViewGroup

class BaseKeyboardObserver(contentView: ViewGroup, keyboardVisibilityListener: (Boolean) -> Unit) {
    private var isKeyboardShowing = false

    init {
        // contentView is the root view of the layout of this activity/fragment
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15f) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true
                    keyboardVisibilityListener(true)
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                    keyboardVisibilityListener(false)
                }
            }
        }
    }
}