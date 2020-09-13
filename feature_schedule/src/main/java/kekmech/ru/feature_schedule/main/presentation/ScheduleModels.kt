package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.LocalDate

typealias ScheduleFeature =
        Feature<ScheduleState, ScheduleEvent, ScheduleEffect>

data class ScheduleState(
    val weekOffset: Int = 0,
    val isLoading: Boolean = true,
    val schedule: MutableMap<Int, Schedule> = mutableMapOf(), // weekOffset -> schedule
    val firstDayOfCurrentWeek: LocalDate? = null
) {

}

sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
        object Action {
            data class WeekDaysStartReached(val leftOffset: Int) : Wish()
            data class WeekDaysEndReached(val rightOffset: Int) : Wish()
        }
    }

    sealed class News : ScheduleEvent() {
        data class ScheduleWeekLoadSuccess(val weekOffset: Int, val schedule: Schedule) : News()
        data class ScheduleWeekLoadError(val throwable: Throwable) : News()
    }
}

sealed class ScheduleEffect {
    data class ShowWeekLoadingError(val throwable: Throwable) : ScheduleEffect()
}

sealed class ScheduleAction {
    data class LoadSchedule(val weekOffset: Int) : ScheduleAction()
}