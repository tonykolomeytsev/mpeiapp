package kekmech.ru.feature_schedule_impl.presentation.screen.main

import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPage
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Everything the [ScheduleScreen] shows, see [ScheduleUiConverter]
 *
 * @param selectedDate selected day, the week calendar and the days pager follow it
 * @param weekOffset offset of the selected week from the current one
 * @param weekOfSemester week of the semester of the selected week, null while it's loading
 * @param dayPages pages of the days pager by week offset: the selected week and its neighbours
 * @param selectedDayOfWeek day of week of the [selectedDate], starting from 1 (Monday)
 * @param savedScheduleBanner banner shown when the selected week couldn't be updated, null if hidden
 * @param fab navigation button to the next or current week, null if it's disabled in the settings
 */
internal data class ScheduleUi(
    val selectedDate: LocalDate,
    val weekOffset: Int,
    val weekOfSemester: WeekOfSemester?,
    val dayPages: Map<Int, List<ScheduleDayPage>>,
    val selectedDayOfWeek: Int,
    val savedScheduleBanner: SavedScheduleBannerUi?,
    val fab: ScheduleFabUi?,
)

/**
 * @param updatedAt when the shown saved schedule was downloaded, null if unknown
 */
internal data class SavedScheduleBannerUi(val updatedAt: Instant?)

/**
 * @param isNextWeek true if the button leads to the next week, false if back to the current one
 * @param isVisible false while the classes are scrolled down, the hidden button is not clickable
 */
internal data class ScheduleFabUi(
    val isNextWeek: Boolean,
    val isVisible: Boolean,
)

internal object ScheduleUiConverter {

    fun map(state: ScheduleState): ScheduleUi =
        ScheduleUi(
            selectedDate = state.selectedDate,
            weekOffset = state.weekOffset,
            weekOfSemester = state.weekOfSemester,
            dayPages = ScheduleClassesListConverter.map(state),
            // Sunday is out of range, the pager stays where it is then
            selectedDayOfWeek = state.selectedDate.dayOfWeek.value,
            savedScheduleBanner = if (state.isShowingSavedSchedule) {
                SavedScheduleBannerUi(updatedAt = state.selectedResource?.updatedAt)
            } else {
                null
            },
            fab = if (state.appSettings.showNavigationButton) {
                ScheduleFabUi(
                    isNextWeek = state.isOnCurrentWeek,
                    isVisible = state.isNavigationFabVisible,
                )
            } else {
                null
            },
        )
}

internal object ScheduleHeaderFormatter {

    /**
     * Title of the selected day, e.g. "Вторник, 7 мая"
     *
     * @param daysOfWeek full names of the days of week starting from Monday
     * @param months names of the months in the genitive case starting from January
     */
    fun formatDay(
        day: LocalDate,
        daysOfWeek: Array<String>,
        months: Array<String>,
    ): String {
        val dayOfWeekName = daysOfWeek.getOrElse(day.dayOfWeek.value - 1) { "" }
        val monthName = months.getOrElse(day.monthValue - 1) { "" }
        return "$dayOfWeekName, ${day.dayOfMonth} $monthName"
    }

    /**
     * Time of the saved schedule download for the banner, e.g. "10 ноября, 14:20"
     */
    fun formatUpdatedAt(
        updatedAt: Instant,
        locale: Locale,
        zoneId: ZoneId = ZoneId.systemDefault(),
    ): String =
        DateTimeFormatter
            .ofPattern("d MMMM, HH:mm", locale)
            .withZone(zoneId)
            .format(updatedAt)
}
