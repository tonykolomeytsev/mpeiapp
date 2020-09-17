package kekmech.ru.feature_schedule.main

import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.feature_schedule.main.item.LunchItem
import kekmech.ru.feature_schedule.main.item.SelfStudyItem
import kekmech.ru.feature_schedule.main.item.ShimmerItem
import kekmech.ru.feature_schedule.main.item.WorkingDayItem
import kekmech.ru.feature_schedule.main.presentation.ScheduleState

object ScheduleClassesListConverter {

    private val shimmerItems = List(6) { ShimmerItem.classes() }

    fun map(state: ScheduleState): List<Any> {
        println(state)
        val selectedWeekSchedule = state.schedule[state.selectedDay.weekOffset]?.weeks?.first()
        return when {
            state.isLoading || selectedWeekSchedule == null -> shimmerItems
            else -> List(6) {
                val dayOfWeek = it + 1
                val rawClasses = selectedWeekSchedule.days.find { day -> day.dayOfWeek == dayOfWeek }?.classes ?: emptyList()
                val modifiedClasses = if (rawClasses.isEmpty()) {
                    listOf(SelfStudyItem)
                } else {
                    withLunchItem(rawClasses)
                }
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = modifiedClasses
                )
            }
        }
    }

    private fun withLunchItem(rawClasses: List<Classes>): List<Any> {
        val hasSecondAndThirdClasses = rawClasses.any { it.number == 2 } && rawClasses.any { it.number == 3 }
        return if (hasSecondAndThirdClasses) {
            val modifiedClasses = mutableListOf<Any>().apply { addAll(rawClasses) }
            modifiedClasses.add(rawClasses.indexOfLast { it.number == 2 } + 1, LunchItem)
            modifiedClasses
        } else {
            rawClasses
        }
    }
}