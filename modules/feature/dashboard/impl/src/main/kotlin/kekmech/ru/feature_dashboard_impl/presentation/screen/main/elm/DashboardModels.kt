package kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm

import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.library_elm.Resource
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
