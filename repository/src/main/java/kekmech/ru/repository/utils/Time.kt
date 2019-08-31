package kekmech.ru.repository.utils

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Time(private val calendar: Calendar = Calendar.getInstance()) {

    constructor(date: Date) : this(Calendar.getInstance().apply { time = date })

    private val hoursMinutesFormatter = SimpleDateFormat("hh:mm", Locale.ENGLISH)

    val dayOfWeek by lazy { calendar.get(Calendar.DAY_OF_WEEK) }
    val dayOfMonth by lazy { calendar.get(Calendar.DAY_OF_MONTH) }
    val dayOfYear by lazy { calendar.get(Calendar.DAY_OF_YEAR) }
    val weekOfMonth by lazy { calendar.get(Calendar.WEEK_OF_MONTH) }
    val weekOfYear by lazy { calendar.get(Calendar.WEEK_OF_YEAR) }
    val month by lazy { calendar.get(Calendar.MONTH) }
    val year by lazy { calendar.get(Calendar.YEAR) }

    /**
     * Форматирование времени к виду "Часы:Минуты"
     */
    val formattedAsHoursMinutes by lazy { hoursMinutesFormatter.format(calendar.time) }

    /**
     * Форматирование к виду "День недели"
     * @param context - контекст через который можно получить доступ к strings.xml
     * @param stringArrayId - массив с названиями дней недели (ВСК, ПН, ВТ, СР ...)
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

    companion object {
        fun today() = Time(Calendar.getInstance(TimeZone.getDefault()))
    }
}