package kekmech.ru.feature_search_impl.screens.schedule_details

import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPage
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPagesConverter
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import java.time.LocalDate

/**
 * Everything the [ScheduleDetailsScreen] shows, see [ScheduleDetailsUiConverter]
 *
 * @param weeksSummary classes counts of this and next weeks, null while any of them is loading
 * @param selectedDate selected day, the week calendar and the days pager follow it
 * @param weekOffset offset of the selected week from the current one
 * @param dayPages pages of the days pager by week offset: the selected week and its neighbours
 * @param favoritesFab button adding the schedule to favorites or removing from them,
 * null while the favorites are loading
 */
internal data class ScheduleDetailsUi(
    val name: String,
    val weeksSummary: ScheduleDetailsWeeksSummaryUi?,
    val selectedDate: LocalDate,
    val weekOffset: Int,
    val dayPages: Map<Int, List<ScheduleDayPage>>,
    val favoritesFab: ScheduleDetailsFavoritesFabUi?,
)

/**
 * A week which failed to load has no classes
 */
internal data class ScheduleDetailsWeeksSummaryUi(
    val thisWeekClassesCount: Int,
    val nextWeekClassesCount: Int,
)

/**
 * @param isInFavorites whether the schedule is in favorites, the button removes it from them then
 * @param isVisible false while the classes are scrolled down, the hidden button is not clickable
 */
internal data class ScheduleDetailsFavoritesFabUi(
    val isInFavorites: Boolean,
    val isVisible: Boolean,
)

/**
 * Grammatical form of the classes count in the week summary, e.g. "1 пара", "2 пары", "5 пар"
 */
internal enum class ClassesCountForm { One, Few, Many }

internal object ScheduleDetailsUiConverter {

    fun map(state: ScheduleDetailsState): ScheduleDetailsUi =
        ScheduleDetailsUi(
            name = state.searchResult.name,
            weeksSummary = mapWeeksSummary(state),
            selectedDate = state.selectedDate,
            weekOffset = state.weekOffset,
            dayPages = mapDayPages(state),
            favoritesFab = state.isInFavorites?.let { isInFavorites ->
                ScheduleDetailsFavoritesFabUi(
                    isInFavorites = isInFavorites,
                    isVisible = state.isFavoritesFabVisible,
                )
            },
        )

    @Suppress("MagicNumber")
    fun classesCountForm(count: Int): ClassesCountForm = when {
        count % 100 in 11..19 -> ClassesCountForm.Many
        count % 10 == 1 -> ClassesCountForm.One
        count % 10 in 2..4 -> ClassesCountForm.Few
        else -> ClassesCountForm.Many
    }

    private fun mapWeeksSummary(state: ScheduleDetailsState): ScheduleDetailsWeeksSummaryUi? {
        val thisWeekClassesCount = state.classesCount(weekOffset = 0) ?: return null
        val nextWeekClassesCount = state.classesCount(weekOffset = 1) ?: return null
        return ScheduleDetailsWeeksSummaryUi(thisWeekClassesCount, nextWeekClassesCount)
    }

    /**
     * Number of the classes of the week, 0 if it failed to load, null while it's loading
     */
    private fun ScheduleDetailsState.classesCount(weekOffset: Int): Int? {
        if (loadingError(weekOffset) != null) return 0
        val resource = schedule[weekOffset]?.takeIf { it.isSettled } ?: return null
        return resource.value
            ?.weeks
            ?.firstOrNull()
            ?.days
            .orEmpty()
            .sumOf { it.classes.size }
    }

    /**
     * The endless days pager shows Saturday of the previous week and Monday of the next one
     * while swiping past the selected week
     */
    private fun mapDayPages(state: ScheduleDetailsState): Map<Int, List<ScheduleDayPage>> =
        (state.weekOffset - 1..state.weekOffset + 1).associateWith { weekOffset ->
            ScheduleDayPagesConverter.map(
                schedule = state.schedule[weekOffset]?.value,
                error = state.loadingError(weekOffset),
            )
        }
}
