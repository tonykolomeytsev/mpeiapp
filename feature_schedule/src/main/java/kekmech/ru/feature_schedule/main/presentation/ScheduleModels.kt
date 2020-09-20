package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.item.WeekItem
import java.time.LocalDate
import java.util.*

typealias ScheduleFeature =
        Feature<ScheduleState, ScheduleEvent, ScheduleEffect>

data class ScheduleState(
    val isFirstLoading: Boolean = true,
    val weekOffset: Int = 0,
    val isLoading: Boolean = true,
    val schedule: MutableMap<Int, Schedule> = mutableMapOf(), // weekOffset -> schedule
    val currentWeekMonday: LocalDate? = null,
    val selectedDay: DayItem = DayItem(LocalDate.now(), 0, true),
    val weekItems: HashMap<Int, WeekItem> = hashMapOf(),
    val hash: String = ""
) {
    /**
     * Get weekOfSemester number by weekOffset
     * @param n - weekOffset
     */
    val weekOfSemester get() = schedule[0]?.weeks?.firstOrNull()?.weekOfSemester?.plus(weekOffset)
}

sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
        object Action {
            data class SelectWeek(val weekOffset: Int) : Wish()
            data class OnPageChanged(val page: Int) : Wish()
        }

        object Click {
            data class OnDayClick(val dayItem: DayItem) : Wish()
        }
    }

    sealed class News : ScheduleEvent() {
        data class ScheduleWeekLoadSuccess(val weekOffset: Int, val schedule: Schedule) : News()
        data class ScheduleWeekLoadError(val throwable: Throwable) : News()
    }
}

sealed class ScheduleEffect {
    data class ShowLoadingError(val throwable: Throwable) : ScheduleEffect()
}

sealed class ScheduleAction {
    data class LoadSchedule(val weekOffset: Int) : ScheduleAction()
}