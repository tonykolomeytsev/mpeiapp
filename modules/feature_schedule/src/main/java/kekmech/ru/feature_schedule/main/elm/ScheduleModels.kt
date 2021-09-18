package kekmech.ru.feature_schedule.main.elm

import kekmech.ru.common_android.moscowLocalDate
import kekmech.ru.common_kotlin.mutableLinkedHashMap
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_schedule.dto.Classes
import kekmech.ru.domain_schedule.dto.Schedule
import java.time.LocalDate
import java.util.*

private const val SCHEDULE_RAM_CACHE_CAPACITY = 5

internal data class ScheduleState(
    val isAfterError: Boolean = false,
    val schedule: MutableMap<Int, Schedule> = mutableLinkedHashMap(SCHEDULE_RAM_CACHE_CAPACITY),
    val weekOffset: Int = 0,
    val selectedDate: LocalDate = moscowLocalDate(),
    val hash: String = "",
    val appSettings: AppSettings,
    val isNavigationFabVisible: Boolean = true,
    val lastError: Throwable? = null
) {
    /**
     * Get weekOfSemester number by weekOffset
     * @param n - weekOffset
     */
    val weekOfSemester get() = schedule[weekOffset]?.weeks?.firstOrNull()?.weekOfSemester
        ?: schedule[0]?.weeks?.firstOrNull()?.weekOfSemester?.plus(weekOffset)

    val selectedSchedule get() = schedule[weekOffset]

    val isOnCurrentWeek get() = weekOffset == 0
}

internal sealed class ScheduleEvent {

    sealed class Wish : ScheduleEvent() {
        object Init : Wish()
        object Action {
            data class SelectWeek(val weekOffset: Int) : Wish()
            data class PageChanged(val page: Int) : Wish()
            object NotesUpdated : Wish()
            object UpdateScheduleIfNeeded : Wish()
            data class ClassesScrolled(val dy: Int) : Wish()
        }

        object Click {
            data class Day(val date: LocalDate) : Wish()
            data class Classes(val classes: kekmech.ru.domain_schedule.dto.Classes) : Wish()
            object FAB : Wish()
        }
    }

    sealed class News : ScheduleEvent() {
        data class ScheduleWeekLoadSuccess(val weekOffset: Int, val schedule: Schedule) : News()
        data class ScheduleWeekLoadError(val throwable: Throwable) : News()
    }
}

internal sealed class ScheduleEffect {
    data class NavigateToNoteList(val classes: Classes, val date: LocalDate) : ScheduleEffect()
}

internal sealed class ScheduleAction {
    data class LoadSchedule(val weekOffset: Int) : ScheduleAction()
}