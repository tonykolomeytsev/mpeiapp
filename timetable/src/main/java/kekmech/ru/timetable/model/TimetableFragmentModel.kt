package kekmech.ru.timetable.model

import kekmech.ru.core.dto.Time
import kekmech.ru.coreui.adapter.BaseItem
import kekmech.ru.timetable.MinCoupleItem

interface TimetableFragmentModel {
    val today: Time

    fun getDaySchedule(dayOfWeek: Int, weekNum: Int): List<BaseItem<*>>
}