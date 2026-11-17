package kekmech.ru.feature_schedule_ui.compose

import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_ui.items.LunchItem
import kekmech.ru.feature_schedule_ui.items.WindowItem
import kekmech.ru.feature_schedule_ui.utils.withLunch
import kekmech.ru.feature_schedule_ui.utils.withWindows

/**
 * Converts one week of a schedule to the pages of the [ScheduleDaysPager], Monday to Saturday
 */
public object ScheduleDayPagesConverter {

    /**
     * Pages of the week which is still loading
     */
    public val loadingPages: List<ScheduleDayPage> = List(WORKING_DAYS_IN_WEEK) {
        ScheduleDayPage(dayOfWeek = it + 1, items = listOf(ScheduleDayItem.Shimmer))
    }

    /**
     * @param schedule the week, null while it's loading or if it failed to load
     * @param error why the week failed to load, the error is shown only if there is no [schedule]
     */
    public fun map(schedule: Schedule?, error: Throwable?): List<ScheduleDayPage> {
        val week = schedule?.weeks?.firstOrNull()
        return when {
            error != null && week == null -> List(WORKING_DAYS_IN_WEEK) {
                ScheduleDayPage(
                    dayOfWeek = it + 1,
                    items = listOf(ScheduleDayItem.Error(error)),
                )
            }

            schedule == null || week == null -> loadingPages
            else -> List(WORKING_DAYS_IN_WEEK) {
                val dayOfWeek = it + 1
                val rawClasses = week.days
                    .find { day -> day.dayOfWeek == dayOfWeek }
                    ?.classes
                    ?.onEach { classes -> classes.scheduleType = schedule.type }
                    .orEmpty()
                val items = if (rawClasses.isEmpty()) {
                    listOf(ScheduleDayItem.SelfStudy(week.firstDayOfWeek.plusDays(it.toLong())))
                } else {
                    rawClasses.toDayItems(schedule.type)
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
