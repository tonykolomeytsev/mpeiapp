package kekmech.ru.coreui

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

typealias OnSystemInsetsChangeListener = (statusBarSize: Int, navigationBarSize: Int) -> Unit

private fun removeSystemInsets(view: View, listener: OnSystemInsetsChangeListener) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->

        val desiredBottomInset = calculateDesiredBottomInset(
            view,
            insets.systemWindowInsetTop,
            insets.systemWindowInsetBottom,
            listener
        )

        ViewCompat.onApplyWindowInsets(
            view,
            WindowInsetsCompat.Builder(insets)
                .setSystemWindowInsets(Insets.of(0, 0, 0, desiredBottomInset))
                .build()
        )
    }
}

fun calculateDesiredBottomInset(
    view: View,
    topInset: Int,
    bottomInset: Int,
    listener: OnSystemInsetsChangeListener
): Int {
    val hasKeyboard = view.isKeyboardAppeared(bottomInset)
    val desiredBottomInset = if (hasKeyboard) bottomInset else 0
    listener(topInset, if (hasKeyboard) 0 else bottomInset)
    return desiredBottomInset
}

private fun View.isKeyboardAppeared(bottomInset: Int) =
    bottomInset / resources.displayMetrics.heightPixels.toDouble() > 0.25

fun Activity.initEdgeToEdge(
    listener: OnSystemInsetsChangeListener = { _, _ -> }
) {
    removeSystemInsets(window.decorView, listener)
    window.navigationBarColor = Color.TRANSPARENT
    window.statusBarColor = Color.TRANSPARENT
}