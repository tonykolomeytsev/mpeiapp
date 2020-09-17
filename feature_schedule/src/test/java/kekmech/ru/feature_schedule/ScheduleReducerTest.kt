package kekmech.ru.feature_schedule

import io.kotest.core.spec.style.BehaviorSpec
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.Week
import kekmech.ru.feature_schedule.main.item.DayItem
import kekmech.ru.feature_schedule.main.presentation.*
import kekmech.ru.feature_schedule.main.utils.TimeUtils.createWeekItem
import org.junit.jupiter.api.Assertions.assertEquals
import java.time.LocalDate
import java.time.Month

class ScheduleReducerTest : BehaviorSpec({
    val reducer = ScheduleReducer()

    Given("Initial state") {
        val state = INITIAL_STATE.copy(isFirstLoading = true)
        When("View created") {
            val result = reducer.reduce(ScheduleEvent.Wish.Init, state)
            Then("Should start loading data") {
                assertEquals(result.actions, listOf(ScheduleAction.LoadSchedule(0)))
            }
            Then("Ui state changed") {
                assertEquals(
                    result.state,
                    INITIAL_STATE.copy(isLoading = true)
                )
            }
            Then("No effects") {
                assertEquals(result.effects, emptyList<ScheduleEffect>())
            }
        }
        When("First data successfully loaded") {
            val result = reducer.reduce(ScheduleEvent.News.ScheduleWeekLoadSuccess(0, SCHEDULE), state)
            Then("UI state changed") {
                assertEquals(
                    AFTER_LOADING_STATE,
                    result.state
                )
            }
            Then("No effects") {
                assertEquals(result.effects, emptyList<ScheduleEffect>())
            }
            Then("No actions") {
                assertEquals(result.actions, emptyList<ScheduleAction>())
            }
        }
    }
    Given("After loading state") {
        val state = AFTER_LOADING_STATE.copy()
        When("Scroll back recyclerView (with weeks)") {
            val result = reducer.reduce(ScheduleEvent.Wish.Action.SelectWeek(-1), state.copy(weekItems = INITIAL_WEEK_ITEMS))
            Then("Prefetch week with offset == -2") {
                assertEquals(
                    state.copy(
                        weekOffset = -1,
                        weekItems = AFTER_SCROLL_BACK_WEEK_ITEMS
                    ),
                    result.state
                )
            }
            Then("Load schedule for weekOffset == -1") {
                assertEquals(
                    listOf(ScheduleAction.LoadSchedule(-1)),
                    result.actions
                )
            }
        }
        When("Scroll forward recyclerView (with weeks)") {
            val result = reducer.reduce(ScheduleEvent.Wish.Action.SelectWeek(1), state.copy(weekItems = INITIAL_WEEK_ITEMS))
            Then("Prefetch week with offset == 2") {
                assertEquals(
                    state.copy(
                        weekOffset = 1,
                        weekItems = AFTER_SCROLL_FORWARD_WEEK_ITEMS
                    ),
                    result.state
                )
            }
            Then("Load schedule for weekOffset == 1") {
                assertEquals(
                    listOf(ScheduleAction.LoadSchedule(1)),
                    result.actions
                )
            }
        }
    }
}) {
    companion object {
        private val INITIAL_STATE = ScheduleState()
        private val CURRENT_MONDAY = LocalDate.of(2020, Month.SEPTEMBER, 14)
        private val SCHEDULE = Schedule(
            groupNumber = "C-12-16",
            groupId = "12345",
            weeks = listOf(Week(
                weekOfSemester = 1,
                weekOfYear = 36,
                firstDayOfWeek = CURRENT_MONDAY,
                days = listOf(
                    Day(
                        dayOfWeek = 1,
                        date = CURRENT_MONDAY,
                        classes = listOf()
                    )
                )
            ))
        )
        private val INITIAL_WEEK_ITEMS get() = hashMapOf(
            -3 to createWeekItem(-3, CURRENT_MONDAY.minusWeeks(3)),
            -2 to createWeekItem(-2, CURRENT_MONDAY.minusWeeks(2)),
            -1 to createWeekItem(-1, CURRENT_MONDAY.minusWeeks(1)),
            0 to createWeekItem(0, CURRENT_MONDAY),
            1 to createWeekItem(1, CURRENT_MONDAY.plusWeeks(1)),
            2 to createWeekItem(2, CURRENT_MONDAY.plusWeeks(2)),
            3 to createWeekItem(3, CURRENT_MONDAY.plusWeeks(3))
        )
        private val AFTER_SCROLL_BACK_WEEK_ITEMS get() = hashMapOf(
            -4 to createWeekItem(-4, CURRENT_MONDAY.minusWeeks(4)),
            -3 to createWeekItem(-3, CURRENT_MONDAY.minusWeeks(3)),
            -2 to createWeekItem(-2, CURRENT_MONDAY.minusWeeks(2)),
            -1 to createWeekItem(-1, CURRENT_MONDAY.minusWeeks(1)),
            0 to createWeekItem(0, CURRENT_MONDAY),
            1 to createWeekItem(1, CURRENT_MONDAY.plusWeeks(1)),
            2 to createWeekItem(2, CURRENT_MONDAY.plusWeeks(2)),
            3 to createWeekItem(3, CURRENT_MONDAY.plusWeeks(3))
        )
        private val AFTER_SCROLL_FORWARD_WEEK_ITEMS get() = hashMapOf(
            -3 to createWeekItem(-3, CURRENT_MONDAY.minusWeeks(3)),
            -2 to createWeekItem(-2, CURRENT_MONDAY.minusWeeks(2)),
            -1 to createWeekItem(-1, CURRENT_MONDAY.minusWeeks(1)),
            0 to createWeekItem(0, CURRENT_MONDAY),
            1 to createWeekItem(1, CURRENT_MONDAY.plusWeeks(1)),
            2 to createWeekItem(2, CURRENT_MONDAY.plusWeeks(2)),
            3 to createWeekItem(3, CURRENT_MONDAY.plusWeeks(3)),
            4 to createWeekItem(4, CURRENT_MONDAY.plusWeeks(4))
        )
        private val AFTER_LOADING_STATE = INITIAL_STATE.copy(
            isFirstLoading = false,
            isLoading = false,
            weekOffset = 0,
            schedule = mutableMapOf(0 to SCHEDULE),
            currentWeekMonday = CURRENT_MONDAY,
            selectedDay = DayItem(LocalDate.now(), 0, false),
            weekItems = INITIAL_WEEK_ITEMS
        )
    }
}