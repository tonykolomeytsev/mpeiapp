package kekmech.ru.feature_schedule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.common_kotlin.mutableLinkedHashMap
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_schedule.dto.Day
import kekmech.ru.domain_schedule.dto.Schedule
import kekmech.ru.domain_schedule.dto.ScheduleType
import kekmech.ru.domain_schedule.dto.Week
import kekmech.ru.feature_schedule.main.elm.ScheduleAction
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent.News
import kekmech.ru.feature_schedule.main.elm.ScheduleEvent.Wish
import kekmech.ru.feature_schedule.main.elm.ScheduleReducer
import kekmech.ru.feature_schedule.main.elm.ScheduleState
import java.time.LocalDate
import java.time.Month

class ScheduleReducerTest : BehaviorSpec({
    val reducer = ScheduleReducer()

    Given("Initial state, Weekday") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE
                state.weekOffset shouldBe 0
                state.isAfterError.shouldBeFalse()
                state.isOnCurrentWeek.shouldBeTrue()
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    ScheduleAction.LoadSchedule(0),
                    ScheduleAction.LoadSchedule(1)
                )
            }
        }
    }
    Given("Initial state, Weekend (Saturday)") {
        val givenState = STATE.copy(selectedDate = CURRENT_DATE_WEEKEND_SAT)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE_WEEKEND_SAT.plusDays(2)
                state.weekOffset shouldBe 1
                state.isAfterError.shouldBeFalse()
                state.isOnCurrentWeek.shouldBeFalse()
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    ScheduleAction.LoadSchedule(0),
                    ScheduleAction.LoadSchedule(1)
                )
            }
        }
    }
    Given("Initial state, Weekend (Sunday)") {
        val givenState = STATE.copy(selectedDate = CURRENT_DATE_WEEKEND_SUN)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE_WEEKEND_SUN.plusDays(1)
                state.weekOffset shouldBe 1
                state.isAfterError.shouldBeFalse()
                state.isOnCurrentWeek.shouldBeFalse()
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    ScheduleAction.LoadSchedule(0),
                    ScheduleAction.LoadSchedule(1)
                )
            }
        }
    }
    Given("While schedule loading") {
        val givenState = STATE
        When("News.ScheduleWeekLoadSuccess (0)") {
            val news = News.ScheduleWeekLoadSuccess(0, SCHEDULE_0)
            val (state, effects, actions) = reducer.reduce(news, givenState)
            Then("Check state") {
                state.schedule.shouldContainExactly(mapOf(0 to SCHEDULE_0))
                state.isAfterError.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.ScheduleWeekLoadError (0)") {
            val news = News.ScheduleWeekLoadError(IllegalStateException("Wake up Neo"))
            val (state, effects, actions) = reducer.reduce(
                news,
                givenState.copy(schedule = mutableLinkedHashMap(CACHE_ENTRIES_SIZE))
            )
            Then("Check state") {
                state.schedule.shouldBeEmpty()
                state.isAfterError.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
    Given("After schedule loading success") {
        val givenState = STATE.copy(schedule = mutableLinkedHashMap(CACHE_ENTRIES_SIZE))
        When("Wish.Action.SelectWeek (1)") {
            val (state, effects, actions) = reducer.reduce(Wish.Action.SelectWeek(1), givenState)
            Then("Check state") {
                state.isAfterError.shouldBeFalse()
                state.selectedDate shouldBe givenState.selectedDate.plusDays(7)
                state.weekOffset shouldBe 1
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleAction.LoadSchedule(1))
            }
        }
        When("Wish.Action.SelectWeek (-1)") {
            val (state, effects, actions) = reducer.reduce(Wish.Action.SelectWeek(-1), givenState)
            Then("Check state") {
                state.isAfterError.shouldBeFalse()
                state.selectedDate shouldBe givenState.selectedDate.minusDays(7)
                state.weekOffset shouldBe -1
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleAction.LoadSchedule(-1))
            }
        }
        When("Wish.Click.Day") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Day(CURRENT_DATE_WEEKEND_SAT), givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE_WEEKEND_SAT
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Click.Day") {
            val (state, effects, actions) = reducer.reduce(Wish.Click.Day(CURRENT_DATE_WEEKEND_SAT), givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE_WEEKEND_SAT
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Action.PageChanged") {
            val (state, effects, actions) = reducer.reduce(Wish.Action.PageChanged(1), givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE_WEEKEND_SAT
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
}) {
    companion object {
        private val APP_SETTINGS = object : AppSettings {
            override val changeDayAfterChangeWeek: Boolean get() = true
            override val isDarkThemeEnabled: Boolean get() = false
            override val autoHideBottomSheet: Boolean get() = false
            override val isSnowEnabled: Boolean get() = false
            override val isDebugEnvironment: Boolean get() = false
            override val languageCode: String get() = "ru_RU"
            override val showNavigationButton: Boolean get() = false
        }
        private const val CACHE_ENTRIES_SIZE = 2
        private val CURRENT_DATE = LocalDate.of(2020, Month.SEPTEMBER, 17)
        private val CURRENT_DATE_WEEKEND_SAT = LocalDate.of(2020, Month.SEPTEMBER, 19) // saturday
        private val CURRENT_DATE_WEEKEND_SUN = LocalDate.of(2020, Month.SEPTEMBER, 20) // saturday
        private val STATE = ScheduleState(
            appSettings = APP_SETTINGS,
            selectedDate = CURRENT_DATE,
            schedule = mutableLinkedHashMap(CACHE_ENTRIES_SIZE)
        )
        private val CURRENT_MONDAY = LocalDate.of(2020, Month.SEPTEMBER, 14)
        private val SCHEDULE_0 = Schedule(
            name = "C-12-16",
            id = "12345",
            type = ScheduleType.GROUP,
            weeks = listOf(Week(
                weekOfSemester = 3,
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
        private val SCHEDULE_1 = Schedule(
            name = "C-12-16",
            id = "12345",
            type = ScheduleType.GROUP,
            weeks = listOf(Week(
                weekOfSemester = 4,
                weekOfYear = 37,
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
        private val SCHEDULE_2 = Schedule(
            name = "C-12-16",
            id = "12345",
            type = ScheduleType.GROUP,
            weeks = listOf(Week(
                weekOfSemester = 5,
                weekOfYear = 38,
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
    }
}