package kekmech.ru.feature_schedule_impl.presentation.screen.main

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDayItem
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDayPage
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_ui.compose.groupParallelClasses
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.feature_schedule_ui.items.LunchItem
import kekmech.ru.feature_schedule_ui.items.WindowItem
import kekmech.ru.feature_schedule_ui.utils.withLunch
import kekmech.ru.feature_schedule_ui.utils.withWindows

internal const val DAY_PAGES_COUNT = 6

internal object ScheduleClassesListConverter {

    /**
     * Pages shown before the first state is rendered
     */
    val loadingPages: List<ScheduleDayPage> = List(DAY_PAGES_COUNT) {
        ScheduleDayPage(dayOfWeek = it + 1, items = listOf(ScheduleDayItem.Shimmer))
    }

    fun map(state: ScheduleState): List<ScheduleDayPage> {
        val selectedSchedule = state.selectedSchedule
        val selectedScheduleWeek = selectedSchedule?.weeks?.firstOrNull()
        val loadingError = state.loadingError
        return when {
            // error state
            loadingError != null && selectedScheduleWeek == null -> List(DAY_PAGES_COUNT) {
                ScheduleDayPage(
                    dayOfWeek = it + 1,
                    items = listOf(ScheduleDayItem.Error(loadingError)),
                )
            }
            selectedSchedule == null || selectedScheduleWeek == null -> loadingPages
            else -> List(DAY_PAGES_COUNT) {
                val dayOfWeek = it + 1
                val rawClasses = selectedScheduleWeek.days
                    .find { day -> day.dayOfWeek == dayOfWeek }
                    ?.classes
                    ?.onEach { classes -> classes.scheduleType = selectedSchedule.type }
                    .orEmpty()
                val items = if (rawClasses.isEmpty()) {
                    listOf(ScheduleDayItem.SelfStudy)
                } else {
                    rawClasses.toDayItems(selectedSchedule.type)
                }
                ScheduleDayPage(dayOfWeek = dayOfWeek, items = items)
            }
        }
    }

    /**
     * Converts the classes of the day to the list items. Parallel classes (e.g. labs of different
     * subgroups) are merged into a single [ScheduleDayItem.ClassesEntry].
     */
    private fun List<Classes>.toDayItems(scheduleType: ScheduleType): List<ScheduleDayItem> {
        // Parallel classes have the same number and time, so the first of them is enough
        // to place the windows and lunch, the rest are attached back by it
        val parallelClassesByFirst = groupParallelClasses().associateBy { it.first() }
        return parallelClassesByFirst.keys.toList()
            .withWindows()
            .withLunch()
            .mapIndexedNotNull { index, item ->
                when (item) {
                    is Classes -> ScheduleDayItem.ClassesEntry(
                        index = index,
                        // Notes are attached by the classes name and time, so the first is used for clicks
                        classes = item,
                        model = parallelClassesByFirst.getValue(item).toUiModel(scheduleType),
                    )
                    is WindowItem -> ScheduleDayItem.Window(index)
                    is LunchItem -> ScheduleDayItem.Lunch
                    else -> null
                }
            }
    }
}
