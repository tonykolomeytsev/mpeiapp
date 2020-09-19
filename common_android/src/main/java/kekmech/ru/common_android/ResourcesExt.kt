package kekmech.ru.common_android

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.roundToInt

fun Resources.dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

fun Resources.dpToPx(dp: Float): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    dp,
    displayMetrics
).roundToInt()

