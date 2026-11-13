package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import java.time.LocalDate

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    val thisWeek: List<Day>? = null,
    val nextWeek: List<Day>? = null,
    /** Null while the favorites are loading */
    val isInFavorites: Boolean? = null,
    /**
     * The schedule is being added to or removed from favorites. [isInFavorites] keeps the previous
     * value meanwhile, so the favorites row stays on the screen and doesn't cut its click ripple.
     */
    val isFavoritesUpdating: Boolean = false,
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
