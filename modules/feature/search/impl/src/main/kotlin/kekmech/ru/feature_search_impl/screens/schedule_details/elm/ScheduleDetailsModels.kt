package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import java.time.LocalDate

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    /** Cached state of each observed week by its week offset */
    val schedule: Map<Int, ScheduleResource> = emptyMap(),
    /** Unexpected failures of week subscriptions by week offset */
    val observeErrors: Map<Int, Throwable> = emptyMap(),
    /** Offset of the selected week from the current one */
    val weekOffset: Int = 0,
    val selectedDate: LocalDate = moscowLocalDate(),
    /** Null while the favorites are loading */
    val isInFavorites: Boolean? = null,
    /**
     * The schedule is being added to or removed from favorites. [isInFavorites] keeps the previous
     * value meanwhile, so the favorites button stays on the screen and doesn't cut its click ripple.
     */
    val isFavoritesUpdating: Boolean = false,
    val favoriteSchedule: FavoriteSchedule? = null,
    /** The favorites button hides while the classes are scrolled down */
    val isFavoritesFabVisible: Boolean = true,
) {

    val scheduleType: ScheduleType get() = searchResult.type

    val selectedResource: ScheduleResource? get() = schedule[weekOffset]

    /** Nothing to show for the week with the [weekOffset] because it failed to load */
    fun loadingError(weekOffset: Int): Throwable? =
        observeErrors[weekOffset] ?: schedule[weekOffset]?.error
}

internal sealed interface ScheduleDetailsEvent {

    sealed interface Ui : ScheduleDetailsEvent {
        object Init : Ui

        object Action {
            /** The week calendar was swiped to the week with the [weekOffset] */
            data class SelectWeek(val weekOffset: Int) : Ui

            /** The days pager was swiped to the [dayOfWeek] (1 is Monday) of the week with the [weekOffset] */
            data class PageChanged(val weekOffset: Int, val dayOfWeek: Int) : Ui
            data class ClassesScrolled(val dy: Int) : Ui
        }

        object Click {
            object Favorites : Ui
            data class Day(val date: LocalDate) : Ui
            object Reload : Ui
        }
    }

    sealed interface Internal : ScheduleDetailsEvent {
        data class ScheduleUpdated(val weekOffset: Int, val resource: ScheduleResource) : Internal
        data class ObserveScheduleFailure(val weekOffset: Int, val throwable: Throwable) : Internal
        data class GetFavoritesSuccess(val schedules: List<FavoriteSchedule>) : Internal
        object RemoveFromFavoritesSuccess : Internal
        data class AddToFavoritesSuccess(val schedule: FavoriteSchedule) : Internal
    }
}

/**
 * The screen has no one-time effects: it's closed by the back navigation only
 */
internal sealed interface ScheduleDetailsEffect

internal sealed interface ScheduleDetailsCommand {

    /**
     * Subscribe to the week of the schedule. Repeating the command restarts the subscription,
     * and the week is refreshed if it's stale.
     */
    data class ObserveSchedule(
        val type: ScheduleType,
        val name: String,
        val weekOffset: Int,
    ) : ScheduleDetailsCommand

    /** Download the week now, the result arrives through [ObserveSchedule] */
    data class RefreshSchedule(val key: ScheduleKey) : ScheduleDetailsCommand

    object GetFavorites : ScheduleDetailsCommand
    data class AddToFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
    data class RemoveFromFavorites(val schedule: FavoriteSchedule) : ScheduleDetailsCommand
}
