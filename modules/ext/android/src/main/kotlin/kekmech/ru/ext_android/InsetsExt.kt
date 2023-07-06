package kekmech.ru.ext_android

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun View.addSystemVerticalPadding() {
    doOnApplyWindowInsets { _, windowInsets, initialPadding ->
        updatePadding(
            top = windowInsets.systemWindowInsetTop + initialPadding.top,
            bottom = windowInsets.systemWindowInsetBottom + initialPadding.bottom
        )
    }
}

fun View.addSystemBottomPadding() {
    doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
    }
}

fun View.addSystemTopPadding() {
    doOnApplyWindowInsets { view, insets, padding ->
        view.updatePadding(top = padding.top + insets.systemWindowInsetTop)
    }
}
