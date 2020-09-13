package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Schedule

typealias ScheduleFeature =
        Feature<ScheduleState, ScheduleEvent, ScheduleEffect>

data class ScheduleState(
    val weekOffset: Int = 0,
    val isLoading: Boolean = true,
    val schedule: Schedule? = null
)

sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
    }

    sealed class News : ScheduleEvent() {

    }
}

sealed class ScheduleEffect {

}

sealed class ScheduleAction {
    data class ObserveSchedule(val weekOffset: Int) : ScheduleAction()
}