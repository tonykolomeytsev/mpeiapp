package kekmech.ru.feature_schedule.main

import kekmech.ru.feature_schedule.main.item.HeaderItem
import kekmech.ru.feature_schedule.main.item.ShimmerItem
import kekmech.ru.feature_schedule.main.presentation.ScheduleState

object ScheduleListConverter {

    fun map(state: ScheduleState): List<Any> {

        return mutableListOf<Any>().apply {
            when {
                state.isLoading || state.currentWeekMonday == null -> {
                    add(ShimmerItem.header())
                    addAll(listOf(ShimmerItem.classes(), ShimmerItem.classes(), ShimmerItem.classes()))
                }
                else -> {
                    add(createHeaderItem(state))
                    addAll(listOf(ShimmerItem.classes(), ShimmerItem.classes(), ShimmerItem.classes()))
                }
            }
        }
    }

    private fun createHeaderItem(state: ScheduleState) = HeaderItem(
        currentWeekMonday = state.currentWeekMonday!!,
        selectedDay = state.selectedDay,
        selectedWeekNumber = state.nullWeekSemesterNumber?.let { it + state.weekOffset } ?: -1
    )
}