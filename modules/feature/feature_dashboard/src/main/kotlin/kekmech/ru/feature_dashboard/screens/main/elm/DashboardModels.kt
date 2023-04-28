package kekmech.ru.feature_dashboard.screens.main.elm

import kekmech.ru.common_kotlin.Resource
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester
import java.time.LocalDate

internal data class DashboardState(
    val selectedSchedule: SelectedSchedule,
    val weekOfSemester: Resource<WeekOfSemester> = Resource.Loading,
    val upcomingEvents: Resource<UpcomingEventsPrediction> = Resource.Loading,
    val actualNotes: Resource<List<Note>> = Resource.Loading,
    val favoriteSchedules: Resource<List<FavoriteSchedule>> = Resource.Loading,
) {

    val isLoading: Boolean =
        listOf(
            weekOfSemester,
            upcomingEvents,
            actualNotes,
            favoriteSchedules
        ).any { it.isLoading }
}

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
        data class GetSelectedScheduleSuccess(val selectedSchedule: SelectedSchedule) : Internal
        data class GetWeekOfSemesterSuccess(val weekOfSemester: WeekOfSemester) : Internal
        data class GetWeekOfSemesterFailure(val throwable: Throwable) : Internal
        data class GetUpcomingEventsSuccess(val upcomingEvents: UpcomingEventsPrediction) : Internal
        data class GetUpcomingEventsFailure(val throwable: Throwable) : Internal
        data class GetActualNotesSuccess(val notes: List<Note>) : Internal
        data class GetActualNotesFailure(val throwable: Throwable) : Internal
        data class GetFavoriteSchedulesSuccess(val favorites: List<FavoriteSchedule>) : Internal
        data class GetFavoriteSchedulesFailure(val throwable: Throwable) : Internal
        object SelectScheduleSuccess : Internal
    }
}

internal sealed interface DashboardEffect {
    object ShowLoadingError : DashboardEffect
    object ShowNotesLoadingError : DashboardEffect
    data class NavigateToNotesList(val classes: Classes, val date: LocalDate) : DashboardEffect
}

internal sealed interface DashboardCommand {
    object GetSelectedSchedule : DashboardCommand
    object GetWeekOfSemester : DashboardCommand
    object GetUpcomingEvents : DashboardCommand
    object GetActualNotes : DashboardCommand
    object GetFavoriteSchedules : DashboardCommand
    data class SelectSchedule(val selectedSchedule: SelectedSchedule) : DashboardCommand
}
