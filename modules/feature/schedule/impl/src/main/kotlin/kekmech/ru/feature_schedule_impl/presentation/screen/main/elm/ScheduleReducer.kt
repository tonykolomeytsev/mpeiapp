package kekmech.ru.feature_schedule_impl.presentation.screen.main.elm

import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent.Internal
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import java.time.DayOfWeek
import java.time.LocalDate
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleCommand as Command
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEffect as Effect
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleEvent as Event
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState as State

internal class ScheduleReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.LoadScheduleSuccess -> {
                state {
                    copy(
                        loadingError = null,
                        schedule = schedule + mapOf(event.weekOffset to event.schedule),
                    )
                }
            }

            is Internal.LoadScheduleFailure -> state { copy(loadingError = event.throwable) }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> {
                state { showNextWeekIfWeekend() }
                commands {
                    +Command.LoadSchedule(0)
                    +Command.LoadSchedule(1)
                }
            }

            is Ui.Action.SelectWeek -> selectWeek(event)
            is Ui.Click.Day -> state {
                copy(
                    selectedDate = event.date,
                    isNavigationFabVisible = true,
                )
            }

            is Ui.Action.PageChanged -> {
                val oldSelectedDay = state.selectedDate.dayOfWeek.value
                val newSelectedDay = event.page + 1L
                state {
                    copy(
                        selectedDate = state
                            .selectedDate.plusDays(newSelectedDay - oldSelectedDay),
                        isNavigationFabVisible = true,
                    )
                }
            }

            is Ui.Click.Classes -> effects {
                +Effect.NavigateToNoteList(
                    classes = event.classes,
                    date = state.selectedDate,
                )
            }

            is Ui.Action.NotesUpdated, is Ui.Action.UpdateScheduleIfNeeded -> commands {
                +Command.LoadSchedule(state.weekOffset)
            }

            is Ui.Action.ClassesScrolled -> state { copy(isNavigationFabVisible = event.dy <= 0) }
            is Ui.Click.FAB -> selectWeek(Ui.Action.SelectWeek(if (state.weekOffset != 0) 0 else 1))
            is Ui.Click.Reload -> commands { +Command.LoadSchedule(state.weekOffset) }
        }
    }

    private fun Result.selectWeek(
        event: Ui.Action.SelectWeek,
    ) {
        if (state.weekOffset == event.weekOffset) return
        state {
            copy(
                weekOffset = event.weekOffset,
                selectedDate = selectNecessaryDate(state, event.weekOffset),
                loadingError = null,
            )
        }
        commands { +Command.LoadSchedule(event.weekOffset) }
    }

    private fun selectNecessaryDate(
        state: State,
        newWeekOffset: Int,
    ): LocalDate = state.selectedDate.plusWeeks((newWeekOffset - state.weekOffset).toLong())

    private fun State.showNextWeekIfWeekend(): State {
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
