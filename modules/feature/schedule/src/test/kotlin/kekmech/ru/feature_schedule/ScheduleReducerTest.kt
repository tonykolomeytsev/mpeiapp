package kekmech.ru.feature_schedule

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.domain_app_settings_models.AppSettings
import kekmech.ru.domain_schedule_models.dto.Classes
import kekmech.ru.domain_schedule_models.dto.ClassesType
import kekmech.ru.domain_schedule_models.dto.Day
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.domain_schedule_models.dto.Week
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester
import kekmech.ru.ext_kotlin.mutableLinkedHashMap
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleCommand
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEffect
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEvent.Internal
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleEvent.Ui
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleReducer
import kekmech.ru.feature_schedule.screens.main.elm.ScheduleState
import kekmech.ru.library_schedule.utils.atStartOfWeek
import java.time.LocalDate
import java.time.Month

class ScheduleReducerTest : BehaviorSpec({
    val reducer = ScheduleReducer()

    Given("Initial state, Weekday") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
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
                    ScheduleCommand.LoadSchedule(0),
                    ScheduleCommand.LoadSchedule(1)
                )
            }
        }
    }
    Given("Initial state, Weekend (Saturday)") {
        val givenState = STATE.copy(selectedDate = CURRENT_DATE_WEEKEND_SAT)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
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
                    ScheduleCommand.LoadSchedule(0),
                    ScheduleCommand.LoadSchedule(1)
                )
            }
        }
    }
    Given("Initial state, Weekend (Sunday)") {
        val givenState = STATE.copy(selectedDate = CURRENT_DATE_WEEKEND_SUN)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
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
                    ScheduleCommand.LoadSchedule(0),
                    ScheduleCommand.LoadSchedule(1)
                )
            }
        }
    }
    Given("While schedule loading") {
        val givenState = STATE
        When("News.ScheduleWeekLoadSuccess (0)") {
            val internal = Internal.LoadScheduleSuccess(0, SCHEDULE_0)
            val (state, effects, actions) = reducer.reduce(internal, givenState)
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
            val internal = Internal.LoadScheduleFailure(IllegalStateException("Wake up Neo"))
            val (state, effects, actions) = reducer.reduce(
                internal,
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
            val (state, effects, actions) = reducer.reduce(Ui.Action.SelectWeek(1), givenState)
            Then("Check state") {
                state.isAfterError.shouldBeFalse()
                state.selectedDate shouldBe givenState.selectedDate.plusDays(7)
                state.weekOffset shouldBe 1
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleCommand.LoadSchedule(1))
            }
        }
        When("Wish.Action.SelectWeek (-1)") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.SelectWeek(-1), givenState)
            Then("Check state") {
                state.isAfterError.shouldBeFalse()
                state.selectedDate shouldBe givenState.selectedDate.minusDays(7)
                state.weekOffset shouldBe -1
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleCommand.LoadSchedule(-1))
            }
        }
        When("Wish.Click.Day") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.Day(CURRENT_DATE_WEEKEND_SAT), givenState)
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
            val (state, effects, actions) = reducer.reduce(Ui.Action.PageChanged(1), givenState)
            Then("Check state") {
                state.selectedDate shouldBe CURRENT_DATE.atStartOfWeek().plusDays(1L)
                state.isNavigationFabVisible.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Click.Classes") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.Classes(CLASSES), givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldContainExactly(ScheduleEffect.NavigateToNoteList(CLASSES, state.selectedDate))
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Click.FAB") {
            val (state, effects, actions) = reducer.reduce(Ui.Click.FAB, givenState)
            Then("Check state") {
                state.weekOffset shouldBe 1
                state.selectedDate shouldBe CURRENT_DATE.plusDays(7L)
                state.isOnCurrentWeek.shouldBeFalse()
                state.isAfterError.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleCommand.LoadSchedule(1))
            }
        }
        When("Wish.Action.NotesUpdated") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.UpdateScheduleIfNeeded, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleCommand.LoadSchedule(0))
            }
        }
        When("Wish.Action.UpdateScheduleIfNeeded") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.UpdateScheduleIfNeeded, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(ScheduleCommand.LoadSchedule(0))
            }
        }
        When("Wish.Action.ClassesScrolled (scrolled down)") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.ClassesScrolled(1), givenState)
            Then("Check state") {
                state.isNavigationFabVisible.shouldBeFalse()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("Wish.Action.ClassesScrolled (scrolled up)") {
            val (state, effects, actions) = reducer.reduce(Ui.Action.ClassesScrolled(-1), givenState)
            Then("Check state") {
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
        private val APP_SETTINGS = AppSettings(
            isDarkThemeEnabled = false,
            autoHideBottomSheet = false,
            isSnowEnabled = false,
            languageCode = "ru_RU",
            showNavigationButton = false,
            mapAppearanceType = "hybrid",
        )
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
                weekOfSemester = WeekOfSemester.Studying(3),
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
        private val CLASSES = Classes(
            name = "Гидропневмопривод мехатронных и робототехнчиеских систем",
            type = ClassesType.PRACTICE,
            rawType = "",
            groups = "С-12-16",
            place = "",
            person = "Зуев Ю.Ю.",
            number = 4
        )
    }
}
