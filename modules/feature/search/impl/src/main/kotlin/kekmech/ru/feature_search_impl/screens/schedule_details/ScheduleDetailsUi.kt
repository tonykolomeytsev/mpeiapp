package kekmech.ru.feature_search_impl.screens.schedule_details

import kekmech.ru.feature_schedule_ui.compose.ParallelClassesUi
import kekmech.ru.feature_schedule_ui.compose.groupParallelClasses
import kekmech.ru.feature_schedule_ui.compose.toUiModel
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import java.time.LocalDate

/**
 * Everything the [ScheduleDetailsScreen] shows, see [ScheduleDetailsUiConverter]
 *
 * @param isInFavorites whether the schedule is in favorites,
 * null while the favorites are loading or updating
 * @param weeks this and next weeks of the schedule, null while any of them is loading
 */
internal data class ScheduleDetailsUi(
    val name: String,
    val description: String,
    val isInFavorites: Boolean?,
    val weeks: ScheduleDetailsWeeksUi?,
)

/**
 * This and next weeks of the schedule. A week which failed to load has no days and no classes.
 *
 * @param days days of both weeks, including Sundays
 * @param selectedDayClasses classes of the selected day, parallel classes are merged
 */
internal data class ScheduleDetailsWeeksUi(
    val thisWeekClassesCount: Int,
    val nextWeekClassesCount: Int,
    val days: List<ScheduleDetailsDayUi>,
    val selectedDayClasses: List<ParallelClassesUi>,
)

internal data class ScheduleDetailsDayUi(
    val date: LocalDate,
    val isSelected: Boolean,
)

/**
 * Grammatical form of the classes count in the week summary, e.g. "1 пара", "2 пары", "5 пар"
 */
internal enum class ClassesCountForm { One, Few, Many }

internal object ScheduleDetailsUiConverter {

    fun map(state: ScheduleDetailsState): ScheduleDetailsUi =
        ScheduleDetailsUi(
            name = state.searchResult.name,
            description = state.searchResult.description,
            isInFavorites = state.isInFavorites,
            weeks = mapWeeks(state),
        )

    @Suppress("MagicNumber")
    fun classesCountForm(count: Int): ClassesCountForm = when {
        count % 100 in 11..19 -> ClassesCountForm.Many
        count % 10 == 1 -> ClassesCountForm.One
        count % 10 in 2..4 -> ClassesCountForm.Few
        else -> ClassesCountForm.Many
    }

    private fun mapWeeks(state: ScheduleDetailsState): ScheduleDetailsWeeksUi? {
        val thisWeek = state.thisWeek ?: return null
        val nextWeek = state.nextWeek ?: return null
        val days = thisWeek + nextWeek
        val selectedDayClasses = days
            .find { it.date == state.selectedDayDate }
            ?.classes
            .orEmpty()
            .groupParallelClasses()
            .map { it.toUiModel(state.scheduleType) }
        return ScheduleDetailsWeeksUi(
            thisWeekClassesCount = thisWeek.sumOf { it.classes.size },
            nextWeekClassesCount = nextWeek.sumOf { it.classes.size },
            days = days.map { ScheduleDetailsDayUi(it.date, isSelected = it.date == state.selectedDayDate) },
            selectedDayClasses = selectedDayClasses,
        )
    }
}
