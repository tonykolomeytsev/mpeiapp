package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.item.WeekItem
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
    val selectedDay: DayItem = DayItem(LocalDate.now(), 0, true),
    val weekItems: HashMap<Int, WeekItem> = hashMapOf(),
    val hash: String = "",
    val appSettings: AppSettings,
    val isAfterError: Boolean = false
) {
    /**
     * Get weekOfSemester number by weekOffset
     * @param n - weekOffset
     */
    val weekOfSemester get() = schedule[0]?.weeks?.firstOrNull()?.weekOfSemester?.plus(weekOffset)
}

internal sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
        object Action {
            data class SelectWeek(val weekOffset: Int) : Wish()
            data class OnPageChanged(val page: Int) : Wish()
            object OnNotesUpdated : Wish()
            object UpdateScheduleIfNeeded : Wish()
        }

        object Click {
            data class OnDayClick(val dayItem: DayItem) : Wish()
            data class OnClassesClick(val classes: Classes) : Wish()
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