package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.common_kotlin.moscowLocalDateTime
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.GROUP_NUMBER_PATTERN
import kekmech.ru.domain_schedule.dto.SessionItem
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.time.LocalDate
import java.time.LocalDateTime

internal data class DashboardState(
    val isLoading: Boolean = false,
    val loadingError: Throwable? = null,
    val currentWeekSchedule: Schedule? = null,
    val nextWeekSchedule: Schedule? = null,
    val selectedScheduleName: String = "",
    val notes: List<Note>? = null,
    val favoriteSchedules: List<FavoriteScheduleItem>? = null,
    val sessionScheduleItems: List<SessionItem>? = null,
    val lastUpdateDateTime: LocalDateTime = moscowLocalDateTime(),
) {
    val weekOfSemester get() = currentWeekSchedule?.weeks?.first()?.weekOfSemester
    val selectedScheduleType: ScheduleType get() = when {
        selectedScheduleName.matches(GROUP_NUMBER_PATTERN) -> ScheduleType.GROUP
        else -> ScheduleType.PERSON
    }
}

internal data class NextClassesTimeStatus(
    val condition: NextClassesCondition,
    val hoursUntilClasses: Long = 0,
    val minutesUntilClasses: Long = 0,
)

internal enum class NextClassesCondition { NOT_STARTED, STARTED, ENDED }

internal data class UpcomingEventsMappingResult(val list: List<Any>, val dayOffset: Int)

internal sealed interface DashboardEvent {

    sealed interface Ui : DashboardEvent {
        object Init : Ui

        object Action {
            object SwipeToRefresh : Ui
            object SilentUpdate : Ui
            data class SelectFavoriteSchedule(val favoriteSchedule: FavoriteSchedule) : Ui
        }

        object Click {
            data class ClassesItem(val classes: Classes) : Ui
        }
    }

    sealed interface Internal : DashboardEvent {
        data class LoadScheduleSuccess(val schedule: Schedule, val weekOffset: Int) : Internal
        data class LoadScheduleFailure(val throwable: Throwable) : Internal
        data class LoadNotesFailure(val throwable: Throwable) : Internal
        data class GetSelectedGroupNameSuccess(val groupName: String) : Internal
        data class LoadNotesSuccess(val notes: List<Note>) : Internal
        data class LoadFavoriteSchedulesSuccess(val favorites: List<FavoriteSchedule>) : Internal
        data class LoadSessionSuccess(val items: List<SessionItem>) : Internal
        object SelectGroupSuccess : Internal
    }
}

internal sealed interface DashboardEffect {
    object ShowLoadingError : DashboardEffect
    object ShowNotesLoadingError : DashboardEffect
    data class NavigateToNotesList(val classes: Classes, val date: LocalDate) : DashboardEffect
}

internal sealed interface DashboardCommand {
    object GetSelectedGroupName : DashboardCommand
    data class LoadSchedule(val weekOffset: Int) : DashboardCommand
    object LoadNotes : DashboardCommand
    object LoadFavoriteSchedules : DashboardCommand
    object LoadSession : DashboardCommand
    data class SelectGroup(val groupName: String) : DashboardCommand
}
