package kekmech.ru.ui_shimmer


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun rememberShimmerBounds(): Rect {
    val displayMetrics = LocalContext.current.resources.displayMetrics
    return remember(displayMetrics) {
        Rect(
            left = 0f,
            top = 0f,
            right = displayMetrics.widthPixels.toFloat(),
            bottom = displayMetrics.heightPixels.toFloat()
        )
    }
}

