package kekmech.ru.feature_schedule.main.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.News
import kekmech.ru.feature_schedule.main.presentation.ScheduleEvent.Wish
import kekmech.ru.feature_schedule.main.utils.TimeUtils.createWeekItem
import java.time.DayOfWeek
import java.util.*

internal typealias ScheduleResult = Result<ScheduleState, ScheduleEffect, ScheduleAction>

internal class ScheduleReducer : BaseReducer<ScheduleState, ScheduleEvent, ScheduleEffect, ScheduleAction> {

    private val prefetchWeekBuffer = 3
    private var isFirstPageChangeIgnored = false

    override fun reduce(
        event: ScheduleEvent,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is News.ScheduleWeekLoadSuccess -> {
            val schedule = state.schedule.apply { put(event.weekOffset, event.schedule) }
            if (state.isFirstLoading) {
                val firstDayOfWeek = event.schedule.weeks.first().firstDayOfWeek
                val weekItems = hashMapOf(
                    -3 to createWeekItem(-3, firstDayOfWeek.minusWeeks(3)),
                    -2 to createWeekItem(-2, firstDayOfWeek.minusWeeks(2)),
                    -1 to createWeekItem(-1, firstDayOfWeek.minusWeeks(1)),
                    0 to createWeekItem(0, firstDayOfWeek),
                    1 to createWeekItem(1, firstDayOfWeek.plusWeeks(1)),
                    2 to createWeekItem(2, firstDayOfWeek.plusWeeks(2)),
                    3 to createWeekItem(3, firstDayOfWeek.plusWeeks(3))
                )
                val actualSelectedDay = state.selectedDay
                    .takeIf { it.date.dayOfWeek != DayOfWeek.SUNDAY } ?: state.selectedDay.plusDays(-1)
                Result(
                    state = state.copy(
                        currentWeekMonday = firstDayOfWeek,
                        isFirstLoading = false,
                        isLoading = false,
                        schedule = schedule,
                        weekItems = weekItems,
                        selectedDay = actualSelectedDay
                    )
                )
            } else {
                Result(
                    state = state.copy(
                        isLoading = false,
                        isAfterError = false,
                        isFirstLoading = false,
                        schedule = schedule,
                        hash = UUID.randomUUID().toString()
                    )
                )
            }
        }
        is News.ScheduleWeekLoadError -> Result(
            state = state.copy(
                isLoading = false,
                isAfterError = true
            ),
        )
    }

    private fun reduceWish(
        event: Wish,
        state: ScheduleState
    ): ScheduleResult = when (event) {
        is Wish.Init -> Result(
            state = state.copy(isLoading = true),
            action = ScheduleAction.LoadSchedule(state.weekOffset)
        )
        is Wish.Action.SelectWeek -> generateSelectedWeekResult(state, event)
        is Wish.Click.OnDayClick -> Result(
            state = state.copy(
                selectedDay = event.dayItem.copy(),
                isNavigationFabCurrentWeek = state.weekOffset == 0
            )
        )
        is Wish.Action.OnPageChanged -> {
            if (!isFirstPageChangeIgnored || state.isLoading) {
                isFirstPageChangeIgnored = true
                Result(state)
            } else {
                val oldSelectedDay = state.selectedDay.dayOfWeek
                val newSelectedDay = event.page + 1
                ScheduleResult(
                    state = state.copy(
                        selectedDay = state.selectedDay.plusDays(newSelectedDay - oldSelectedDay)
                    )
                )
            }
        }
        is Wish.Click.OnClassesClick -> Result(
            state = state,
            effect = ScheduleEffect.NavigateToNoteList(event.classes, state.selectedDay.date)
        )
        is Wish.Action.OnNotesUpdated, is Wish.Action.UpdateScheduleIfNeeded -> Result(
            state = state.copy(isLoading = true),
            action = ScheduleAction.LoadSchedule(state.weekOffset)
        )
        is Wish.Action.OnClassesScroll -> Result(
            state = state.copy(isNavigationFabVisible = event.dy <= 0)
        )
        is Wish.Click.OnFAB -> {
            generateSelectedWeekResult(
                state,
                Wish.Action.SelectWeek(if (state.weekOffset != 0) 0 else 1),
                forceChangeSelectedDay = true
            )
        }
    }

    private fun generateSelectedWeekResult(
        state: ScheduleState,
        event: Wish.Action.SelectWeek,
        forceChangeSelectedDay: Boolean = false
    ): Result<ScheduleState, ScheduleEffect, ScheduleAction> {
        if (state.weekOffset == event.weekOffset || state.currentWeekMonday == null) return Result(state)
        val copyOfState = state.copy()
        val weekItems = copyOfState.weekItems
        // prefetch next week WeekItem
        when {
            event.weekOffset < 0 -> {
                val nextWeekOffset = event.weekOffset - prefetchWeekBuffer
                weekItems.getOrPut(nextWeekOffset) {
                    createWeekItem(
                        weekOffset = nextWeekOffset,
                        firstDayOfWeek = checkNotNull(state.currentWeekMonday).plusWeeks(nextWeekOffset.toLong())
                    )
                }
            }
            event.weekOffset > 0 -> {
                val nextWeekOffset = event.weekOffset + prefetchWeekBuffer
                weekItems.getOrPut(nextWeekOffset) {
                    createWeekItem(
                        weekOffset = nextWeekOffset,
                        firstDayOfWeek = checkNotNull(state.currentWeekMonday).plusWeeks(nextWeekOffset.toLong())
                    )
                }
            }
        }
        return Result(
            state = state.copy(
                weekOffset = event.weekOffset,
                weekItems = weekItems,
                selectedDay = selectNecessaryDay(state, event.weekOffset, forceChangeSelectedDay),
                isLoading = true
            ),
            action = ScheduleAction.LoadSchedule(event.weekOffset)
        )
    }

    private fun selectNecessaryDay(
        state: ScheduleState,
        newWeekOffset: Int,
        force: Boolean = false
    ): DayItem {
        val oldSelectedDay = state.selectedDay
        if (state.appSettings.changeDayAfterChangeWeek || force) {
            val oldWeekOffset = oldSelectedDay.weekOffset.toLong()
            return DayItem(
                date = oldSelectedDay.date.plusWeeks(newWeekOffset - oldWeekOffset),
                weekOffset = newWeekOffset,
                isSelected = true
            )
        } else {
            return oldSelectedDay
        }
    }
}