package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_mvi.Feature
import kekmech.ru.common_schedule.items.DayItem
import kekmech.ru.common_schedule.items.WeekItem
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.LocalDate
import java.util.*

internal typealias ScheduleFeature =
        Feature<ScheduleState, ScheduleEvent, ScheduleEffect>

internal data class ScheduleState(
    val isFirstLoading: Boolean = true,
    val weekOffset: Int = 0,
    val isLoading: Boolean = true,
    val schedule: MutableMap<Int, Schedule> = mutableMapOf(), // weekOffset -> schedule
    val currentWeekMonday: LocalDate? = null,
    val selectedDay: DayItem = DayItem(moscowLocalDate(), 0, true),
    val weekItems: HashMap<Int, WeekItem> = hashMapOf(),
    val hash: String = "",
    val appSettings: AppSettings,
    val isAfterError: Boolean = false,
    val isNavigationFabVisible: Boolean = true,
    val isNavigationFabCurrentWeek: Boolean = true
) {
    /**
     * Get weekOfSemester number by weekOffset
     * @param n - weekOffset
     */
    val weekOfSemester get() = schedule[0]?.weeks?.firstOrNull()?.weekOfSemester?.plus(weekOffset)

    val selectedSchedule get() = schedule[selectedDay.weekOffset]
}

internal sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
        object Action {
            data class SelectWeek(val weekOffset: Int) : Wish()
            data class OnPageChanged(val page: Int) : Wish()
            object OnNotesUpdated : Wish()
            object UpdateScheduleIfNeeded : Wish()
            data class OnClassesScroll(val dy: Int) : Wish()
        }

        object Click {
            data class OnDayClick(val dayItem: DayItem) : Wish()
            data class OnClassesClick(val classes: Classes) : Wish()
            object OnFAB : Wish()
        }
    }

    sealed class News : ScheduleEvent() {
        data class ScheduleWeekLoadSuccess(val weekOffset: Int, val schedule: Schedule) : News()
        data class ScheduleWeekLoadError(val throwable: Throwable) : News()
    }
}

internal sealed class ScheduleEffect {
    data class NavigateToNoteList(val classes: Classes, val date: LocalDate) : ScheduleEffect()
}

internal sealed class ScheduleAction {
    data class LoadSchedule(val weekOffset: Int) : ScheduleAction()
}