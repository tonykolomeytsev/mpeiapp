package kekmech.ru.feature_dashboard.presentation

import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_schedule.dto.Classes

typealias DashboardFeature = Feature<DashboardState, DashboardEvent, DashboardEffect>

data class DashboardState(
    val isAfterError: Boolean = false,
    val todayClasses: List<Classes> = emptyList()
)

sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object Init : Wish()

        object Action {
            object OnSwipeRefresh : Wish()
        }
    }

    sealed class News : DashboardEvent() {
        data class TodayScheduleLoaded(val listOfClasses: List<Classes>) : News()
        data class TodayScheduleLoadError(val throwable: Throwable) : News()
    }
}

sealed class DashboardEffect {
    object ShowLoadingError : DashboardEffect()
}

sealed class DashboardAction {
    object LoadTodaySchedule : DashboardAction()
}