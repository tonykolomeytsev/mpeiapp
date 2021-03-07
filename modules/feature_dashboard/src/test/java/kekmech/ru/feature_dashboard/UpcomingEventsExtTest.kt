package kekmech.ru.feature_dashboard

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import kekmech.ru.domain_schedule.dto.*
import kekmech.ru.feature_dashboard.elm.DashboardState
import kekmech.ru.feature_dashboard.upcoming_events.getDayWithOffset
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class UpcomingEventsExtTest : BehaviorSpec({
    Given("DashboardState.getDayWithOffset") {
        val givenState = STATE.copy(currentWeekSchedule = SCHEDULE_0)
        When("Get today classes") {
            val day = givenState.getDayWithOffset(currentDay = DATE, offset = 0)
            Then("Check day") {
                day.shouldNotBeNull()
                day.classes.shouldContainExactly(CLASSES_1)
            }
        }
        When("Get tomorrow classes") {
            val day = givenState.getDayWithOffset(currentDay = DATE, offset = 1)
            Then("Check day") {
                day.shouldNotBeNull()
                day.classes.shouldContainExactly(CLASSES_1, CLASSES_2)
            }
        }
        When("Get after tomorrow classes") {
            val day = givenState.getDayWithOffset(currentDay = DATE, offset = 3)
            Then("Check day") {
                day.shouldNotBeNull()
                day.classes.shouldContainExactly(CLASSES_3, CLASSES_4)
            }
        }
    }
}) {
    private companion object {
        private val CLASSES_1 = Classes(name = "one", number = 1,
            time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55))
        )
        private val CLASSES_2 = Classes(name = "two", number = 2,
            time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45))
        )
        private val CLASSES_3 = Classes(name = "three", number = 3,
            time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20))
        )
        private val CLASSES_4 = Classes(name = "four", number = 4)
        private val CLASSES_5 = Classes(name = "five", number = 5)
        private val DATE = LocalDate.of(1, Month.MARCH, 2021)
        private val SCHEDULE_0 = Schedule(
            weeks = listOf(Week(
                days = listOf(
                    Day(DayOfWeek.MONDAY.value, date = DATE, classes = listOf(CLASSES_1)),
                    Day(DayOfWeek.TUESDAY.value, date = DATE.plusDays(1), classes = listOf(CLASSES_1, CLASSES_2)),
                    Day(DayOfWeek.WEDNESDAY.value, date = DATE.plusDays(2), classes = listOf(CLASSES_3, CLASSES_4)),
                    Day(DayOfWeek.THURSDAY.value, date = DATE.plusDays(3), classes = listOf(CLASSES_4, CLASSES_5))
                )
            ))
        )
        private val STATE = DashboardState()
    }
}