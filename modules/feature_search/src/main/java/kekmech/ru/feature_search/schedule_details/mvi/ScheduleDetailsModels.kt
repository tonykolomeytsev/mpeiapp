package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.*
import java.time.LocalDate

internal typealias ScheduleDetailsFeature = Feature<ScheduleDetailsState, ScheduleDetailsEvent, ScheduleDetailsEffect>

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    val thisWeek: List<Day>? = null,
    val nextWeek: List<Day>? = null,
    val isInFavorites: Boolean? = null,
    val selectedDayDate: LocalDate = moscowLocalDate(),
    val favoriteSchedule: FavoriteSchedule? = null
) {
    val isLoadingSchedule: Boolean get() = thisWeek == null || nextWeek == null
    val scheduleType get() = when (searchResult.type) {
        SearchResultType.GROUP -> ScheduleType.GROUP
        SearchResultType.PERSON -> ScheduleType.PERSON
    }
}

internal sealed class ScheduleDetailsEvent {

    sealed class Wish : ScheduleDetailsEvent() {
        object Init : Wish()

        object Click {
            object Favorites : Wish()
            data class Day(val date: LocalDate) : Wish()
        }
    }

    sealed class News : ScheduleDetailsEvent() {
        data class ScheduleLoaded(val schedule: Schedule, val weekOffset: Int) : News()
        data class LoadScheduleError(val weekOffset: Int) : News()
        data class FavoritesLoaded(val schedules: List<FavoriteSchedule>) : News()
        object FavoriteRemoved : News()
        data class FavoriteAdded(val schedule: FavoriteSchedule) : News()
    }
}

internal sealed class ScheduleDetailsEffect

internal sealed class ScheduleDetailsAction {
    data class LoadSchedule(val ownerName: String, val weekOffset: Int) : ScheduleDetailsAction()
    object LoadFavorites : ScheduleDetailsAction()
    data class AddToFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsAction()
    data class RemoveFromFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsAction()
}