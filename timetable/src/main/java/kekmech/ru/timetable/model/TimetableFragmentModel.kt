package kekmech.ru.timetable.model

import androidx.lifecycle.LiveData
import kekmech.ru.core.dto.Time
import kekmech.ru.coreui.adapter.BaseItem

interface TimetableFragmentModel {
    val today: Time
    /**
     * Group number like "C-12-16"
     */
    val groupNumber: LiveData<String>

    /**
     * Current week number
     */
    val currentWeekNumber: Int

    val formattedTodayStatus: String

    fun getDaySchedule(dayOfWeek: Int, weekNum: Int): List<BaseItem<*>>

    var weekOffset: LiveData<Int>
}