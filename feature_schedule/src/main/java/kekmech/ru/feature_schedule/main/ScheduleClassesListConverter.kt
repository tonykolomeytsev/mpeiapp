package kekmech.ru.feature_schedule.main

import kekmech.ru.feature_schedule.main.item.ShimmerItem
import kekmech.ru.feature_schedule.main.item.WorkingDayItem
import kekmech.ru.feature_schedule.main.presentation.ScheduleState

object ScheduleClassesListConverter {

    private val shimmerItems = List(6) { ShimmerItem.classes() }

    fun map(state: ScheduleState): List<Any> {
        println(state)
        val selectedWeekSchedule = state.schedule[state.weekOffset]?.weeks?.first()
        return when {
            state.isLoading || selectedWeekSchedule == null -> shimmerItems
            else -> List(6) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = selectedWeekSchedule.days.find { day -> day.dayOfWeek == dayOfWeek }?.classes ?: emptyList()
                )
            }
        }
    }
}