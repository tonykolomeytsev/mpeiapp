package kekmech.ru.feature_schedule.main

import kekmech.ru.common_schedule.items.SelfStudyItem
import kekmech.ru.common_schedule.utils.withLunch
import kekmech.ru.common_schedule.utils.withNotePreview
import kekmech.ru.common_schedule.utils.withWindows
import kekmech.ru.coreui.items.EmptyStateItem
import kekmech.ru.coreui.items.ShimmerItem
import kekmech.ru.coreui.items.SpaceItem
import kekmech.ru.feature_schedule.R
import kekmech.ru.feature_schedule.main.elm.ScheduleState
import kekmech.ru.feature_schedule.main.item.WorkingDayItem

private const val DAY_ITEMS_COUNT = 6

internal object ScheduleClassesListConverter {

    fun map(state: ScheduleState): List<Any> {
        val selectedSchedule = state.selectedSchedule
        val selectedScheduleWeek = selectedSchedule?.weeks?.firstOrNull()
        val shouldShowEmptyState = state.isAfterError && selectedScheduleWeek == null
        return when {
            shouldShowEmptyState -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(SpaceItem.VERTICAL_24, getEmptyStateItem())
                )
            }
            selectedSchedule == null || selectedScheduleWeek == null -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                WorkingDayItem(
                    dayOfWeek = dayOfWeek,
                    items = listOf(ShimmerItem(SHIMMER_ITEM_ID))
                )
            }
            else -> List(DAY_ITEMS_COUNT) {
                val dayOfWeek = it + 1
                val rawClasses = selectedScheduleWeek.days
                    .find { day -> day.dayOfWeek == dayOfWeek }
                    ?.classes
                    ?.onEach { classes -> classes.scheduleType = selectedSchedule.type }
                    ?: emptyList()
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

    private fun getEmptyStateItem() = EmptyStateItem(
        titleRes = R.string.schedule_empty_state_title,
        subtitleRes = R.string.schedule_empty_state_subtitle
    )
}