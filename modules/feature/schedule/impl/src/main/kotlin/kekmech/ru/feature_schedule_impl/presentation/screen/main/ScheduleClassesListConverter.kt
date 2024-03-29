package kekmech.ru.feature_schedule_impl.presentation.screen.main

import kekmech.ru.coreui.items.ErrorStateItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_schedule_impl.R
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_impl.presentation.screen.main.item.WorkingDayItem
import kekmech.ru.lib_schedule.items.SelfStudyItem
import kekmech.ru.lib_schedule.utils.withLunch
import kekmech.ru.lib_schedule.utils.withNotePreview
import kekmech.ru.lib_schedule.utils.withWindows

private const val DAY_ITEMS_COUNT = 6

internal object ScheduleClassesListConverter {

    fun map(state: ScheduleState): List<Any> {
        val selectedSchedule = state.selectedSchedule
        val selectedScheduleWeek = selectedSchedule?.weeks?.firstOrNull()
        return when {
            // error state
            state.loadingError != null && selectedScheduleWeek == null -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(SpaceItem.VERTICAL_24, ErrorStateItem(state.loadingError))
                )
            }
            selectedSchedule == null || selectedScheduleWeek == null -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(ShimmerItem(R.layout.item_working_day_shimmer))
                )
            }
            else -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                val rawClasses = selectedScheduleWeek.days
                    .find { day -> day.dayOfWeek == dayOfWeek }
                    ?.classes
                    ?.onEach { classes -> classes.scheduleType = selectedSchedule.type }
                    .orEmpty()
                val modifiedClasses = if (rawClasses.isEmpty()) {
                    listOf(SelfStudyItem)
                } else {
                    rawClasses
                        .withWindows()
                        .withLunch()
                        .withNotePreview()
                }
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = modifiedClasses
                )
            }
        }
    }
}
