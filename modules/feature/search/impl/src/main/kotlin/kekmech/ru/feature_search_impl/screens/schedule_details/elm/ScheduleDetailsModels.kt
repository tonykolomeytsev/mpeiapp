package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.Day
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.ext_kotlin.moscowLocalDate
import java.time.LocalDate

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    val thisWeek: List<Day>? = null,
    val nextWeek: List<Day>? = null,
    val isInFavorites: Boolean? = null,
    val selectedDayDate: LocalDate = moscowLocalDate(),
    val favoriteSchedule: FavoriteSchedule? = null,
) {

    val isLoadingSchedule: Boolean get() = thisWeek == null || nextWeek == null
    val scheduleType get() = searchResult.type
}

internal sealed interface ScheduleDetailsEvent {

    sealed interface Ui : ScheduleDetailsEvent {
        object Init : Ui

        object Click {
            object Favorites : Ui
            data class Day(val date: LocalDate) : Ui
            object SwitchSchedule : Ui
        }
    }

    sealed interface Internal : ScheduleDetailsEvent {
        data class LoadScheduleSuccess(val schedule: Schedule, val weekOffset: Int) : Internal
        data class LoadScheduleError(val weekOffset: Int) : Internal
        data class GetFavoritesSuccess(val schedules: List<FavoriteSchedule>) : Internal
        object RemoveFromFavoritesSuccess : Internal
        data class AddToFavoritesSuccess(val schedule: FavoriteSchedule) : Internal
    }
}

internal sealed interface ScheduleDetailsEffect {
    object CloseAndGoToSchedule : ScheduleDetailsEffect
}

internal sealed interface ScheduleDetailsCommand {
    data class LoadSchedule(
        val type: ScheduleType,
        val name: String,
        val weekOffset: Int,
    ) : ScheduleDetailsCommand

    object GetFavorites : ScheduleDetailsCommand
    data class AddToFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
    data class RemoveFromFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
    data class SwitchSchedule(val selectedSchedule: SelectedSchedule) : ScheduleDetailsCommand
}
