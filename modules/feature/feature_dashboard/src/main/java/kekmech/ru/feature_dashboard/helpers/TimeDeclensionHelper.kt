package kekmech.ru.feature_dashboard.helpers

import android.content.Context
import kekmech.ru.common_android.getStringArray
import kekmech.ru.feature_dashboard.R
import kekmech.ru.strings.Strings

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

    fun formatTimePrediction(context: Context, offset: Int): String? = when (offset) {
        -1 -> null
        0 -> context.getString(Strings.dashboard_events_today)
        1 -> context.getString(Strings.dashboard_events_tomorrow)
        2 -> context.getString(Strings.dashboard_events_after_tomorrow)
        else -> {
            val decDays = context.getStringArray(R.array.days_declensions)
            val adoptedOffset = offset - 1L
            context.getString(Strings.dashboard_events_n_days, format(decDays, adoptedOffset))
        }
    }
}