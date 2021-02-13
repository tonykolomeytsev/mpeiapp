package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.SearchResult

internal typealias ScheduleDetailsFeature = Feature<ScheduleDetailsState, ScheduleDetailsEvent, ScheduleDetailsEffect>

internal data class ScheduleDetailsState(
    val searchResult: SearchResult,
    val thisWeekSchedule: List<Classes>? = null,
    val nextWeekSchedule: List<Classes>? = null
) {
    val isLoading = thisWeekSchedule == null || nextWeekSchedule == null
}

internal sealed class ScheduleDetailsEvent {

    sealed class Wish : ScheduleDetailsEvent() {
        object Init : Wish()
    }

    sealed class News : ScheduleDetailsEvent() {
        data class ScheduleLoaded(val schedule: Schedule, val weekOffset: Int) : News()
        data class LoadScheduleError(val weekOffset: Int) : News()
    }
}

internal sealed class ScheduleDetailsEffect

internal sealed class ScheduleDetailsAction {
    data class LoadSchedule(val ownerName: String, val weekOffset: Int) : ScheduleDetailsAction()
}