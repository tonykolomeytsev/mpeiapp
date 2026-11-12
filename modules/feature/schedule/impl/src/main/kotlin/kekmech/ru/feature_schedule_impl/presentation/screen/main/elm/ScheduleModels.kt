package kekmech.ru.feature_schedule_impl.presentation.screen.main.elm

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import java.time.LocalDate

internal data class ScheduleState(
    /** Cached state of each observed week by its week offset */
    val schedule: Map<Int, ScheduleResource> = emptyMap(),
    /** Unexpected failures of week subscriptions by week offset */
    val observeErrors: Map<Int, Throwable> = emptyMap(),

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
        get() = selectedSchedule?.weeks?.firstOrNull()?.weekOfSemester

    val selectedResource: ScheduleResource? get() = schedule[weekOffset]

    val selectedSchedule: Schedule? get() = selectedResource?.value

    val isOnCurrentWeek get() = weekOffset == 0

    /** Nothing to show for the selected week because it failed to load */
    val loadingError: Throwable?
        get() = observeErrors[weekOffset] ?: selectedResource?.error

    val isAfterError get() = loadingError != null

    /** The selected week is shown from the cache because it couldn't be updated */
    val isShowingSavedSchedule get() = selectedResource?.isSavedAfterFailedRefresh == true
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
            data class Classes(
                val classes: kekmech.ru.feature_schedule_api.domain.model.Classes,
            ) : Ui

            object FAB : Ui
            object Reload : Ui
            object SavedScheduleBanner : Ui
        }
    }

    sealed interface Internal : ScheduleEvent {
        data class ScheduleUpdated(val weekOffset: Int, val resource: ScheduleResource) : Internal
        data class ObserveScheduleFailure(val weekOffset: Int, val throwable: Throwable) : Internal
    }
}

internal sealed interface ScheduleEffect {
    data class NavigateToNoteList(
        val classes: kekmech.ru.feature_schedule_api.domain.model.Classes,
        val date: LocalDate,
    ) : ScheduleEffect
}

internal sealed interface ScheduleCommand {
    /**
     * Subscribe to the week of the selected schedule. Repeating the command restarts the
     * subscription: notes are attached again and the week is refreshed if it's stale.
     */
    data class ObserveSchedule(val weekOffset: Int) : ScheduleCommand

    /** Download the week now, the result arrives through [ObserveSchedule] */
    data class RefreshSchedule(val key: ScheduleKey) : ScheduleCommand
}
