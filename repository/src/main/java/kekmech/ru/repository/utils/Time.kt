package kekmech.ru.repository.utils

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Time(private val calendar: Calendar = Calendar.getInstance()) {

    constructor(date: Date) : this(Calendar.getInstance().apply { time = date })

    constructor(year: Int, month: Int, dayOfMonth: Int) : this(Calendar.getInstance().apply { set(year, month, dayOfMonth) })

    private val hoursMinutesFormatter = SimpleDateFormat("hh:mm", Locale.ENGLISH)

    val dayOfWeek by lazy { calendar.get(Calendar.DAY_OF_WEEK) }
    val dayOfMonth by lazy { calendar.get(Calendar.DAY_OF_MONTH) }
    val dayOfYear by lazy { calendar.get(Calendar.DAY_OF_YEAR) }
    val weekOfMonth by lazy { calendar.get(Calendar.WEEK_OF_MONTH) }
    val weekOfYear by lazy { calendar.get(Calendar.WEEK_OF_YEAR) }
    val month by lazy { calendar.get(Calendar.MONTH) }
    val year by lazy { calendar.get(Calendar.YEAR) }

    val semester by lazy { if (month in Calendar.FEBRUARY..Calendar.AUGUST) SemesterType.SPRING else SemesterType.FALL }
    val weekOfSemester by lazy {
        val firstDay = if (semester == SemesterType.FALL) fallSemesterFirstDay() else springSemesterFirstDay()
        return@lazy 1 + weekOfYear - firstDay.weekOfYear
    }
    val parity by lazy { if (weekOfSemester % 2 == 0) Parity.EVEN else Parity.ODD }

    /**
     * Форматирование времени к виду "Часы:Минуты"
     */
    val formattedAsHoursMinutes by lazy { hoursMinutesFormatter.format(calendar.time) }

    /**
     * Форматирование к виду "День недели"
     * @param context - контекст через который можно получить доступ к strings.xml
     * @param stringArrayId - массив с названиями дней недели (ВСК, ПН, ВТ, СР ...)
     * @return [String] - соответствующее номеру названия дня недели
     * *Обрати внимание, что воскресенье считается первым днем недели (id = 1)
     */
    fun formattedAsDayName(context: Context?, stringArrayId: Int) = getStringArray(context, stringArrayId)[dayOfWeek - 1]

    private fun getStringArray(context: Context?, stringArrayId: Int): Array<String> {
        if (context == null) {
            Log.e(this::class.java.simpleName, "Does not found string array resource")
            return emptyArray()
        }
        return context.resources.getStringArray(stringArrayId)
    }

    enum class Parity { ODD, EVEN }

    enum class SemesterType { FALL, SPRING }

    companion object {
        fun today() = Time(Calendar.getInstance())

        /**
         * Всегда первое сентября
         */
        fun fallSemesterFirstDay(year: Int = today().year) = Time(year, Calendar.SEPTEMBER, 1)

        /**
         * Первый понедельник февраля
         */
        fun springSemesterFirstDay(year: Int = today().year): Time {
            var febDayNum = 1
            var day = Time(year, Calendar.FEBRUARY, febDayNum++)
            while (day.dayOfWeek != Calendar.MONDAY)
                day = Time(year, Calendar.FEBRUARY, febDayNum++)
            return day
        }
    }
}