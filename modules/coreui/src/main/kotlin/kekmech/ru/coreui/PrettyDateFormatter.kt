package kekmech.ru.coreui

import android.content.Context
import kekmech.ru.common_android.getStringArray
import kekmech.ru.strings.StringArrays
import kekmech.ru.strings.Strings
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PrettyDateFormatter(context: Context) {
    private val listOfDayNames = context.getStringArray(StringArrays.days_of_week)
    private val listOfMonths = context.getStringArray(StringArrays.months)
    private val today = context.getString(Strings.today)
    private val tomorrow = context.getString(Strings.tomorrow)
    private val afterTomorrow = context.getString(Strings.after_tomorrow)
    private val yesterday = context.getString(Strings.yesterday)
    private val beforeYesterday = context.getString(Strings.before_yesterday)

    @Suppress("MagicNumber")
    fun formatRelative(date: LocalDate): String {
        val now = LocalDate.now()
        val deltaDays = ChronoUnit.DAYS.between(now, date)
        return when (deltaDays) {
            0L -> today
            1L -> tomorrow
            2L -> afterTomorrow
            -1L -> yesterday
            -2L -> beforeYesterday
            else -> {
                val dayOfWeek = listOfDayNames
                    .getOrNull(date.dayOfWeek.value - 1)
                    .orEmpty()

                val month = listOfMonths
                    .getOrNull(date.monthValue - 1)
                    .orEmpty()

                val dayOfMonth = date.dayOfMonth
                "$dayOfWeek, $dayOfMonth $month"
            }
        }
    }

    fun formatAbsolute(date: LocalDate): String {
        val dayOfWeek = listOfDayNames
            .getOrNull(date.dayOfWeek.value - 1)
            .orEmpty()

        val month = listOfMonths
            .getOrNull(date.monthValue - 1)
            .orEmpty()

        val dayOfMonth = date.dayOfMonth
        return "$dayOfWeek, $dayOfMonth $month"
    }
}
