package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.DayOfWeek
import java.time.LocalDate

typealias DashboardFeature = Feature<DashboardState, DashboardEvent, DashboardEffect>

data class DashboardState(
    val isLoading: Boolean = false,
    val isAfterError: Boolean = false,
    val currentWeekSchedule: Schedule? = null,
    val nextWeekSchedule: Schedule? = null,
    val selectedGroupName: String? = null
) {
    val weekOfSemester get() = currentWeekSchedule?.weeks?.first()?.weekOfSemester
    val todayClasses: List<Classes>? get() = currentWeekSchedule?.weeks?.first()?.days
        ?.find { it.dayOfWeek == LocalDate.now().dayOfWeek.value }
        ?.classes
    val tomorrowClasses: List<Classes>? get() {
        val dayOfWeek = LocalDate.now().dayOfWeek
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return nextWeekSchedule?.weeks?.first()?.days
                ?.find { it.dayOfWeek == DayOfWeek.MONDAY.value }
                ?.classes
        } else {
            return currentWeekSchedule?.weeks?.first()?.days
                ?.find { it.dayOfWeek == dayOfWeek.value + 1 }
                ?.classes
        }
    }
    val isSwipeRefreshLoadingAnimation = isLoading && currentWeekSchedule != null
}

sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object Init : Wish()

        object Action {
            object OnSwipeRefresh : Wish()
        }
    }

    sealed class News : DashboardEvent() {
        data class ScheduleLoaded(val schedule: Schedule, val weekOffset: Int) : News()
        data class ScheduleLoadError(val throwable: Throwable) : News()
        data class SelectedGroupNameLoaded(val groupName: String) : News()
    }
}

sealed class DashboardEffect {
    object ShowLoadingError : DashboardEffect()
}

sealed class DashboardAction {
    object GetSelectedGroupName : DashboardAction()
    data class LoadSchedule(val weekOffset: Int) : DashboardAction()
}