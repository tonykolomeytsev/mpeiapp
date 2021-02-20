package kekmech.ru.feature_schedule.main.elm

import kekmech.ru.feature_schedule.main.elm.ScheduleEvent.News
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*

internal class ScheduleReducer : StateReducer<ScheduleEvent, ScheduleState, ScheduleEffect, ScheduleAction> {

    override fun reduce(
        event: ScheduleEvent,
        state: ScheduleState
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    @Suppress("MagicNumber")
    private fun reduceNews(
        event: News,
        state: ScheduleState
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> = when (event) {
        is News.ScheduleWeekLoadSuccess -> {
            val schedule = state.schedule.apply { put(event.weekOffset, event.schedule) }
            Result(
                state = state.copy(
                    isAfterError = false,
                    schedule = schedule,
                    hash = UUID.randomUUID().toString()
                )
            )
        }
        is News.ScheduleWeekLoadError -> Result(state.copy(isAfterError = true))
    }

    private fun reduceWish(
        event: Wish,
        state: ScheduleState
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> = when (event) {
        is Wish.Init -> Result(
            state = state.showNextWeekIfWeekend(),
            commands = listOf(
                ScheduleAction.LoadSchedule(0),
                ScheduleAction.LoadSchedule(1)
            )
        )
        is Wish.Action.SelectWeek -> getWeekSelectionResult(state, event)
        is Wish.Click.Day -> Result(
            state = state.copy(
                selectedDate = event.date,
                isNavigationFabVisible = true
            )
        )
        is Wish.Action.PageChanged -> {
            val oldSelectedDay = state.selectedDate.dayOfWeek.value
            val newSelectedDay = event.page + 1L
            Result(
                state = state.copy(
                    selectedDate = state.selectedDate.plusDays(newSelectedDay - oldSelectedDay),
                    isNavigationFabVisible = true
                )
            )
        }
        is Wish.Click.Classes -> Result(
            state = state,
            effect = ScheduleEffect.NavigateToNoteList(event.classes, state.selectedDate)
        )
        is Wish.Action.NotesUpdated, is Wish.Action.UpdateScheduleIfNeeded -> Result(
            state = state,
            command = ScheduleAction.LoadSchedule(state.weekOffset)
        )
        is Wish.Action.ClassesScrolled -> Result(
            state = state.copy(isNavigationFabVisible = event.dy <= 0)
        )
        is Wish.Click.FAB -> {
            getWeekSelectionResult(
                state,
                Wish.Action.SelectWeek(if (state.weekOffset != 0) 0 else 1),
                forceChangeSelectedDay = true
            )
        }
    }

    private fun getWeekSelectionResult(
        state: ScheduleState,
        event: Wish.Action.SelectWeek,
        forceChangeSelectedDay: Boolean = false
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> {
        if (state.weekOffset == event.weekOffset) return Result(state)
        return Result(
            state = state.copy(
                weekOffset = event.weekOffset,
                selectedDate = selectNecessaryDate(state, event.weekOffset, forceChangeSelectedDay),
                isAfterError = false
            ),
            command = ScheduleAction.LoadSchedule(event.weekOffset)
        )
    }

    private fun selectNecessaryDate(
        state: ScheduleState,
        newWeekOffset: Int,
        force: Boolean = false
    ): LocalDate {
        if (state.appSettings.changeDayAfterChangeWeek || force) {
            return state.selectedDate.plusWeeks((newWeekOffset - state.weekOffset).toLong())
        } else {
            return state.selectedDate
        }
    }

    private fun ScheduleState.showNextWeekIfWeekend(): ScheduleState {
        val todayIsSunday = selectedDate.dayOfWeek == DayOfWeek.SUNDAY
        val todayIsSaturday = selectedDate.dayOfWeek == DayOfWeek.SATURDAY
        return when {
            todayIsSunday -> copy(
                selectedDate = selectedDate.plusDays(1),
                weekOffset = weekOffset + 1
            )
            todayIsSaturday -> copy(
                selectedDate = selectedDate.plusDays(2),
                weekOffset = weekOffset + 1
            )
            else -> this
        }
    }
}