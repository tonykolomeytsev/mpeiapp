package kekmech.ru.feature_schedule.main

import kekmech.ru.feature_schedule.main.item.HeaderItem
import kekmech.ru.feature_schedule.main.item.ShimmerItem
import kekmech.ru.feature_schedule.main.presentation.ScheduleState
import java.time.LocalDate

object ScheduleListConverter {

    private var headerItem: HeaderItem? = null

    fun map(state: ScheduleState): List<Any> {

        return mutableListOf<Any>().apply {
            when {
                state.isLoading || state.currentWeekMonday == null -> {
                    add(ShimmerItem.header())
                    addAll(listOf(ShimmerItem.classes(), ShimmerItem.classes(), ShimmerItem.classes()))
                }
                else -> {
                    add(createHeaderItem(state.currentWeekMonday))
                    addAll(listOf(ShimmerItem.classes(), ShimmerItem.classes(), ShimmerItem.classes()))
                }
            }
        }
    }

    private fun createHeaderItem(currentWeekMonday: LocalDate): HeaderItem {
        if (headerItem == null) headerItem = HeaderItem(currentWeekMonday)
        return headerItem!!
    }
}