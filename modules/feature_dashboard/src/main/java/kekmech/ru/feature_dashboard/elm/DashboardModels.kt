package kekmech.ru.feature_dashboard.elm

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.coreui.items.FavoriteScheduleItem
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.GROUP_NUMBER_PATTERN
import kekmech.ru.domain_schedule.dto.*
import java.time.DayOfWeek
import java.time.LocalDate

data class DashboardState(
    val isLoading: Boolean = false,
    val isAfterError: Boolean = false,
    val currentWeekSchedule: Schedule? = null,
    val nextWeekSchedule: Schedule? = null,
    val selectedScheduleName: String = "",
    val notes: List<Note>? = null,
    val favoriteSchedules: List<FavoriteScheduleItem>? = null,
    val isFeatureBannerEnabled: Boolean = false,
    val sessionScheduleItems: List<SessionItem>? = null
) {
    val weekOfSemester get() = currentWeekSchedule?.weeks?.first()?.weekOfSemester
    val today: Day? get() = currentWeekSchedule?.weeks?.first()?.days
        ?.find { it.date == moscowLocalDate() }
    val todayClasses: List<Classes>? get() = today?.classes
    val tomorrow: Day? get() {
        val dayOfWeek = moscowLocalDate().dayOfWeek
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return nextWeekSchedule?.weeks?.first()?.days
                ?.find { it.dayOfWeek == DayOfWeek.MONDAY.value }
        } else {
            return currentWeekSchedule?.weeks?.first()?.days
                ?.find { it.dayOfWeek == dayOfWeek.value + 1 }
        }
    }
    val tomorrowClasses: List<Classes>? get() = tomorrow?.classes
    val isSwipeRefreshLoadingAnimation = isLoading && currentWeekSchedule != null
    val selectedScheduleType: ScheduleType get() = when {
        selectedScheduleName.matches(GROUP_NUMBER_PATTERN) -> ScheduleType.GROUP
        else -> ScheduleType.PERSON
    }
}

data class NextClassesTimeStatus(
    val condition: NextClassesCondition,
    val hoursUntilClasses: Long = 0,
    val minutesUntilClasses: Long = 0
)

enum class NextClassesCondition { NOT_STARTED, STARTED, ENDED }

sealed class DashboardEvent {

    sealed class Wish : DashboardEvent() {
        object Init : Wish()

        object Action {
            object OnSwipeRefresh : Wish()
            object SilentUpdate : Wish()
            data class SelectFavoriteSchedule(val favoriteSchedule: FavoriteSchedule) : Wish()
            object CloseFeatureBanner : Wish()
        }

        object Click {
            data class OnClasses(val classes: Classes) : Wish()
        }
    }

    sealed class News : DashboardEvent() {
        data class ScheduleLoaded(val schedule: Schedule, val weekOffset: Int) : News()
        data class ScheduleLoadError(val throwable: Throwable) : News()
        data class NotesLoadError(val throwable: Throwable) : News()
        data class SelectedGroupNameLoaded(val groupName: String) : News()
        data class NotesLoaded(val notes: List<Note>) : News()
        data class FavoriteSchedulesLoaded(val favorites: List<FavoriteSchedule>) : News()
        data class SessionLoaded(val items: List<SessionItem>) : News()
        object FavoriteGroupSelected : News()
    }
}

sealed class DashboardEffect {
    object ShowLoadingError : DashboardEffect()
    object ShowNotesLoadingError : DashboardEffect()
    data class NavigateToNotesList(val classes: Classes, val date: LocalDate) : DashboardEffect()
}

sealed class DashboardAction {
    object GetSelectedGroupName : DashboardAction()
    data class LoadSchedule(val weekOffset: Int) : DashboardAction()
    object LoadNotes : DashboardAction()
    object LoadFavoriteSchedules : DashboardAction()
    object LoadSession : DashboardAction()
    data class SelectGroup(val groupName: String) : DashboardAction()
}