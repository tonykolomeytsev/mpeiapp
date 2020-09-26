package kekmech.ru.feature_schedule.main

import kekmech.ru.common_kotlin.addIf
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.ClassesStackType
import kekmech.ru.feature_schedule.main.item.*
import kekmech.ru.feature_schedule.main.presentation.ScheduleState

object ScheduleClassesListConverter {

    fun map(state: ScheduleState): List<Any> {
        val selectedWeekSchedule = state.schedule[state.selectedDay.weekOffset]?.weeks?.first()
        return when {
            (state.isLoading && state.appSettings.changeDayAfterChangeWeek) || selectedWeekSchedule == null -> List(6) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(ShimmerItem.classes())
                )
            }
            else -> List(6) {
                val dayOfWeek = it + 1
                val rawClasses = selectedWeekSchedule.days.find { day -> day.dayOfWeek == dayOfWeek }?.classes ?: emptyList()
                val modifiedClasses = if (rawClasses.isEmpty()) {
                    listOf(SelfStudyItem)
                } else {
                    withLunchAndWindowItems(rawClasses)
                }
                detectStackClasses(modifiedClasses)
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = modifiedClasses
                )
            }
        }
    }

    private fun withLunchAndWindowItems(rawClasses: List<Classes>): List<Any> {
        val hasSecondAndThirdClasses = rawClasses.any { it.number == 2 } && rawClasses.any { it.number == 3 }
        val modifiedClasses = classesWithWindows(rawClasses)
        if (hasSecondAndThirdClasses) {
            val indexForLunchItem = modifiedClasses.indexOfLast { it is Classes && it.number == 2 } + 1
            modifiedClasses.addAll(indexForLunchItem, listOf(LunchItem))
        }
        return modifiedClasses
    }

    private fun classesWithWindows(rawClasses: List<Classes>): MutableList<Any> {
        val modifiedClasses = mutableListOf<Any>()
        if (rawClasses.size > 1) {
            for (i in 1 until rawClasses.size) {
                val currClasses = rawClasses[i]
                val prevClasses = rawClasses[i - 1]
                modifiedClasses.addIf(
                    WindowItem(
                        timeStart = prevClasses.time.end,
                        timeEnd = currClasses.time.start
                    )
                ) { rawClasses[i].number - rawClasses[i - 1].number > 1 }
                modifiedClasses.add(rawClasses[i])
            }
        } else {
            modifiedClasses.addAll(rawClasses)
        }
        return modifiedClasses
    }

    private fun detectStackClasses(classes: List<Any>) {
        for (i in classes.indices) {
            val currentItem = classes[i] as? Classes
            val nextItem = classes.getOrNull(i + 1) as? Classes
            if (currentItem != null && nextItem != null && currentItem.number == nextItem.number) {
                currentItem.stackType = ClassesStackType.START
                nextItem.stackType = ClassesStackType.MIDDLE
            }
            if (currentItem != null && currentItem.number != nextItem?.number && currentItem.stackType == ClassesStackType.MIDDLE) {
                currentItem.stackType = ClassesStackType.END
            }
        }
    }
}