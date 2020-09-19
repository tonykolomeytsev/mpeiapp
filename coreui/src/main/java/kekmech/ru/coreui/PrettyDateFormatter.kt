package kekmech.ru.coreui

import android.content.Context
import kekmech.ru.common_android.getStringArray
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PrettyDateFormatter(context: Context) {
    private val listOfDayNames = context.getStringArray(R.array.days_of_week)
    private val listOfMonths = context.getStringArray(R.array.months)
    private val today = context.getString(R.string.today)
    private val tomorrow = context.getString(R.string.tomorrow)
    private val afterTomorrow = context.getString(R.string.after_tomorrow)

    fun formatRelative(date: LocalDate): String {
        val now = LocalDate.now()
        val deltaDays = ChronoUnit.DAYS.between(now, date)
        return when (deltaDays) {
            0L -> today
            1L -> tomorrow
            2L -> afterTomorrow
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