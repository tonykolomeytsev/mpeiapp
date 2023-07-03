package kekmech.ru.feature_schedule.screens.main.elm

import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_app_settings_api.domain.AppSettings
import java.time.LocalDate

private const val SCHEDULE_RAM_CACHE_CAPACITY = 5

internal data class ScheduleState(
    val schedule: Map<Int, Schedule> = LinkedHashMap(SCHEDULE_RAM_CACHE_CAPACITY),
    val loadingError: Throwable? = null,

    // region settings
    val weekOffset: Int = 0,
    val selectedDate: LocalDate = moscowLocalDate(),

    // region ui
    val appSettings: AppSettings,
    val isNavigationFabVisible: Boolean = true,
) {

    /**
     * Get weekOfSemester number by weekOffset
     */
    val weekOfSemester
        get() = schedule[weekOffset]?.weeks?.firstOrNull()?.weekOfSemester

    val selectedSchedule get() = schedule[weekOffset]

    val isOnCurrentWeek get() = weekOffset == 0

    val isAfterError get() = loadingError != null
}

internal sealed interface ScheduleEvent {

    sealed interface Ui : ScheduleEvent {
        object Init : Ui
        object Action {
            data class SelectWeek(val weekOffset: Int) : Ui
            data class PageChanged(val page: Int) : Ui
            object NotesUpdated : Ui
            object UpdateScheduleIfNeeded : Ui
            data class ClassesScrolled(val dy: Int) : Ui
        }

        object Click {
            data class Day(val date: LocalDate) : Ui
            data class Classes(val classes: kekmech.ru.domain_schedule_models.dto.Classes) : Ui
            object FAB : Ui
            object Reload : Ui
        }
    }

    sealed interface Internal : ScheduleEvent {
        data class LoadScheduleSuccess(val weekOffset: Int, val schedule: Schedule) : Internal
        data class LoadScheduleFailure(val throwable: Throwable) : Internal
    }
}

internal sealed interface ScheduleEffect {
    data class NavigateToNoteList(val classes: Classes, val date: LocalDate) : ScheduleEffect
}

internal sealed interface ScheduleCommand {
    data class LoadSchedule(val weekOffset: Int) : ScheduleCommand
}
