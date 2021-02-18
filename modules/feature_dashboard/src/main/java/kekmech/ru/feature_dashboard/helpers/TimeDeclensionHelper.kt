package kekmech.ru.feature_dashboard.helpers

import android.content.Context
import kekmech.ru.common_android.getStringArray
import kekmech.ru.feature_dashboard.R

private const val DECLENSION_5_TO_10 = 2
private const val DECLENSION_2_TO_4 = 1
private const val DECLENSION_1 = 0

object TimeDeclensionHelper {

    fun formatHoursMinutes(context: Context, h: Long, m: Long): String {
        val decHours = context.getStringArray(R.array.hours_declensions)
        val decMinutes = context.getStringArray(R.array.minutes_declensions)
        return "${format(decHours, h)} ${format(decMinutes, m)}".trim()
    }

    @Suppress("MagicNumber")
    private fun format(declensions: Array<String>, n: Long): String {
        if (n == 0L) return ""
        if (n in 11L..19L) return "$n " + declensions[DECLENSION_5_TO_10]
        return "$n " + when (n % 10L) {
            1L -> declensions[DECLENSION_1]
            in 2L..4L -> declensions[DECLENSION_2_TO_4]
            else -> declensions[DECLENSION_5_TO_10]
        }
    }
}