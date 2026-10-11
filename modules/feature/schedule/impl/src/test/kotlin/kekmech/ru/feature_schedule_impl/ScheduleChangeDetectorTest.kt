package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.notifications.ScheduleChange.ChangeType
import kekmech.ru.feature_schedule_impl.notifications.ScheduleChangeDetector
import java.time.LocalDate
import java.time.LocalTime

class ScheduleChangeDetectorTest : BehaviorSpec({

    val date = LocalDate.of(2026, 10, 12)

    fun classes(
        number: Int = 1,
        name: String = "Математика",
        place: String = "Е-101",
        person: String = "Иванов И.И.",
        start: LocalTime = LocalTime.of(9, 20),
    ) = Classes(
        name = name,
        type = ClassesType.LECTURE,
        rawType = null,
        place = place,
        groups = "А-01-21",
        person = person,
        time = Time(start = start, end = start.plusMinutes(90)),
        number = number,
    )

    fun schedule(vararg classes: Classes) = Schedule(
        name = "А-01-21",
        id = "1",
        type = ScheduleType.GROUP,
        weeks = listOf(
            Week(
                weekOfYear = 42,
                weekOfSemester = WeekOfSemester.Studying(8),
                firstDayOfWeek = date,
                days = listOf(Day(dayOfWeek = 1, date = date, classes = classes.toList())),
            )
        ),
    )

    Given("No cached schedule") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = null,
            newSchedule = schedule(classes()),
        )
        Then("nothing is reported") {
            changes.shouldBeEmpty()
        }
    }

    Given("Identical schedules") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes()),
            newSchedule = schedule(classes()),
        )
        Then("no changes are detected") {
            changes.shouldBeEmpty()
        }
    }

    Given("A class disappeared") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes(number = 1), classes(number = 2, name = "Физика")),
            newSchedule = schedule(classes(number = 1)),
        )
        Then("it is reported as removed") {
            changes shouldHaveSize 1
            changes.single().type shouldBe ChangeType.REMOVED
            changes.single().date shouldBe date
            changes.single().oldClass?.name shouldBe "Физика"
        }
    }

    Given("A new class appeared") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes(number = 1)),
            newSchedule = schedule(classes(number = 1), classes(number = 2, name = "Физика")),
        )
        Then("it is reported as added") {
            changes shouldHaveSize 1
            changes.single().type shouldBe ChangeType.ADDED
            changes.single().newClass?.name shouldBe "Физика"
        }
    }

    Given("A class changed its room") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes(place = "Е-101")),
            newSchedule = schedule(classes(place = "Е-205")),
        )
        Then("it is reported as modified") {
            changes shouldHaveSize 1
            changes.single().type shouldBe ChangeType.MODIFIED
        }
    }

    Given("A class changed its time") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes(start = LocalTime.of(9, 20))),
            newSchedule = schedule(classes(start = LocalTime.of(11, 10))),
        )
        Then("it is reported as modified") {
            changes shouldHaveSize 1
            changes.single().type shouldBe ChangeType.MODIFIED
        }
    }

    Given("A class was replaced by another one in the same slot") {
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = schedule(classes(name = "Математика")),
            newSchedule = schedule(classes(name = "Физика")),
        )
        Then("it is reported as replaced") {
            changes shouldHaveSize 1
            changes.single().type shouldBe ChangeType.REPLACED
        }
    }

    Given("A week missing in the old schedule") {
        val old = schedule(classes()).copy(
            weeks = schedule(classes()).weeks.map { it.copy(weekOfYear = 41) }
        )
        val changes = ScheduleChangeDetector.detectChanges(
            oldSchedule = old,
            newSchedule = schedule(classes()),
        )
        Then("nothing is reported") {
            changes.shouldBeEmpty()
        }
    }
})
