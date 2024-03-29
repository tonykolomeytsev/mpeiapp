package kekmech.ru.feature_dashboard_impl.presentation.screen.main.helpers

import android.content.Context
import kekmech.ru.ext_android.getStringArray
import kekmech.ru.res_strings.R.array as StringArrays
import kekmech.ru.res_strings.R.string as Strings

private const val DECLENSION_5_TO_10 = 2
private const val DECLENSION_2_TO_4 = 1
private const val DECLENSION_1 = 0

internal object TimeDeclensionHelper {

    fun formatHoursMinutes(context: Context, h: Long, m: Long): String {
        val decHours = context.getStringArray(StringArrays.hours_declensions)
        val decMinutes = context.getStringArray(StringArrays.minutes_declensions)
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

    fun formatTimePrediction(context: Context, dayOffset: Int): String? = when (dayOffset) {
        -1 -> null
        0 -> context.getString(Strings.dashboard_events_today)
        1 -> context.getString(Strings.dashboard_events_tomorrow)
        2 -> context.getString(Strings.dashboard_events_after_tomorrow)
        else -> {
            val decDays = context.getStringArray(StringArrays.days_declensions)
            val adoptedOffset = dayOffset - 1L
            context.getString(Strings.dashboard_events_n_days, format(decDays, adoptedOffset))
        }
    }
}
