package kekmech.ru.feature_search.schedule_details.elm

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.ScheduleType
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule.dto.SearchResultType
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
    val scheduleType get() = when (searchResult.type) {
        SearchResultType.GROUP -> ScheduleType.GROUP
        SearchResultType.PERSON -> ScheduleType.PERSON
    }
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
        data class LoadFavoritesSuccess(val schedules: List<FavoriteSchedule>) : Internal
        object RemoveFromFavoritesSuccess : Internal
        data class AddToFavoritesSuccess(val schedule: FavoriteSchedule) : Internal
    }
}

internal sealed interface ScheduleDetailsEffect {
    object CloseAndGoToSchedule : ScheduleDetailsEffect
}

internal sealed interface ScheduleDetailsCommand {
    data class LoadSchedule(val ownerName: String, val weekOffset: Int) : ScheduleDetailsCommand
    object LoadFavorites : ScheduleDetailsCommand
    data class AddToFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
    data class RemoveFromFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
    data class SwitchSchedule(val scheduleName: String) : ScheduleDetailsCommand
}
