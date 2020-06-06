package kekmech.ru.common_android.views

import android.view.View
import android.view.ViewGroup

fun ViewGroup.forEach(block: (View) -> Unit) {
    (0 until childCount).forEach { block(getChildAt(it)) }
}