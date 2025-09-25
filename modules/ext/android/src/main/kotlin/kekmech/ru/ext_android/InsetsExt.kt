package kekmech.ru.ext_android

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

public fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialPadding) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialPadding)
        insets
    }
    requestApplyInsetsWhenAttached()
}

public data class InitialPadding(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
)

private fun recordInitialPaddingForView(view: View) =
    InitialPadding(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

public fun View.requestApplyInsetsWhenAttached() {
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

public fun View.addSystemVerticalPadding() {
    doOnApplyWindowInsets { _, windowInsets, initialPadding ->
        val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        updatePadding(
            top = systemBarsInsets.top + initialPadding.top,
            bottom = systemBarsInsets.bottom + initialPadding.bottom
        )
    }
}

public fun View.addSystemBottomPadding() {
    doOnApplyWindowInsets { view, insets, padding ->
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(bottom = padding.bottom + systemBarsInsets.bottom)
    }
}

public fun View.addSystemTopPadding() {
    doOnApplyWindowInsets { view, insets, padding ->
        val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(top = padding.top + systemBarsInsets.top)
    }
}
