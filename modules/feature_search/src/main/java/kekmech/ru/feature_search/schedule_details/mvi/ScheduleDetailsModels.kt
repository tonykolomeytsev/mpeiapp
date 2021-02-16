package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.*

internal typealias ScheduleDetailsFeature = Feature<ScheduleDetailsState, ScheduleDetailsEvent, ScheduleDetailsEffect>

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    val thisWeek: List<Day>? = null,
    val nextWeek: List<Day>? = null,
    val isInFavorites: Boolean? = null
) {
    val isLoadingSchedule: Boolean get() = thisWeek == null || nextWeek == null
    private val scheduleType get() = when (searchResult.type) {
        SearchResultType.GROUP -> ScheduleType.GROUP
        SearchResultType.PERSON -> ScheduleType.PERSON
    }

    fun getThisWeekClasses(): List<Classes>? =
        thisWeek?.flatMap { day -> day.classes.onEach { it.scheduleType = scheduleType } }

    fun getNextWeekClasses(): List<Classes>? =
        nextWeek?.flatMap { day -> day.classes.onEach { it.scheduleType = scheduleType } }
}

internal sealed class ScheduleDetailsEvent {

    sealed class Wish : ScheduleDetailsEvent() {
        object Init : Wish()

        object Click {
            object Favorites : Wish()
        }
    }

    sealed class News : ScheduleDetailsEvent() {
        data class ScheduleLoaded(val schedule: Schedule, val weekOffset: Int) : News()
        data class LoadScheduleError(val weekOffset: Int) : News()
    }
}

internal sealed class ScheduleDetailsEffect

internal sealed class ScheduleDetailsAction {
    data class LoadSchedule(val ownerName: String, val weekOffset: Int) : ScheduleDetailsAction()
    object LoadFavorites : ScheduleDetailsAction()
    data class AddToFavorites(val scheduleName: String) : ScheduleDetailsAction()
    data class RemoveFromFavorites(val scheduleName: String) : ScheduleDetailsAction()
}