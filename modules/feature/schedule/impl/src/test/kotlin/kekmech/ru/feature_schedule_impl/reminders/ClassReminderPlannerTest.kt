package kekmech.ru.feature_schedule_impl.reminders

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPlanner
import kekmech.ru.feature_schedule_impl.notifications.reminders.ReminderKind
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class ClassReminderPlannerTest : BehaviorSpec({

    val zone = ZoneId.of("Europe/Moscow")
    val date = LocalDate.of(2026, 10, 14)
    val both = setOf(ReminderKind.DAY_BEFORE, ReminderKind.HOURS_BEFORE)

    fun at(day: LocalDate, time: LocalTime) = ZonedDateTime.of(day, time, zone)
    fun millis(day: LocalDate, time: LocalTime) = at(day, time).toInstant().toEpochMilli()

    fun scheduleOf(vararg classes: Classes) = Schedule(
        name = "А-01-21",
        id = "1",
        type = ScheduleType.GROUP,
        weeks = listOf(
            Week(
                weekOfYear = 42,
                weekOfSemester = WeekOfSemester.Studying(7),
                firstDayOfWeek = date.minusDays(2),
                days = listOf(Day(dayOfWeek = 3, date = date, classes = classes.toList())),
            ),
        ),
    )

    fun classes(
        type: ClassesType,
        start: LocalTime = LocalTime.of(12, 20),
        name: String = "Физика",
    ) = Classes(
        name = name,
        type = type,
        rawType = null,
        place = "Е-101",
        groups = "А-01-21",
        person = "Иванов И.И.",
        time = Time(start = start, end = start.plusMinutes(90)),
        number = 3,
    )

    val longBefore = at(date.minusDays(3), LocalTime.of(10, 0)).toInstant()

    Given("a day with a lecture, a practice and a lab") {
        val schedule = scheduleOf(
            classes(ClassesType.LECTURE, name = "Лекция"),
            classes(ClassesType.PRACTICE, name = "Практика"),
            classes(ClassesType.LAB, start = LocalTime.of(14, 10), name = "Лаба"),
        )

        When("planning both reminder kinds") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, both)

            Then("lectures are skipped, practice and lab get 24h and 5h reminders") {
                reminders.map { it.name }.toSet() shouldBe setOf("Практика", "Лаба")
                reminders shouldHaveSize 4
            }
            Then("triggers are exactly 24 and 5 hours before the start") {
                val practice = reminders.filter { it.name == "Практика" }.associateBy { it.kind }
                practice.getValue(ReminderKind.DAY_BEFORE).triggerAtMillis shouldBe
                    millis(date.minusDays(1), LocalTime.of(12, 20))
                practice.getValue(ReminderKind.HOURS_BEFORE).triggerAtMillis shouldBe
                    millis(date, LocalTime.of(7, 20))
            }
            Then("reminders are sorted by the trigger time and have distinct stable ids") {
                reminders.map { it.triggerAtMillis } shouldBe reminders.map { it.triggerAtMillis }.sorted()
                reminders.map { it.id }.toSet() shouldHaveSize 4
                ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, both).map { it.id } shouldBe
                    reminders.map { it.id }
            }
        }

        When("only the 5 hour reminder is enabled") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, setOf(ReminderKind.HOURS_BEFORE))

            Then("only 5h reminders are planned") {
                reminders.map { it.kind }.toSet() shouldBe setOf(ReminderKind.HOURS_BEFORE)
            }
        }

        When("no reminder kind is enabled") {
            Then("nothing is planned") {
                ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, emptySet()).shouldBeEmpty()
            }
        }

        When("the 24h moment of the practice has already passed") {
            val now = at(date.minusDays(1), LocalTime.of(15, 0)).toInstant()
            val reminders = ClassReminderPlanner.plan(listOf(schedule), now, zone, both)
                .filter { it.name == "Практика" }

            Then("only the 5h reminder is left") {
                reminders.map { it.kind } shouldBe listOf(ReminderKind.HOURS_BEFORE)
            }
        }

        When("the class has already started") {
            val now = at(date, LocalTime.of(13, 0)).toInstant()

            Then("the practice is not reminded any more") {
                ClassReminderPlanner.plan(listOf(schedule), now, zone, both)
                    .none { it.name == "Практика" } shouldBe true
            }
        }
    }

    Given("an early morning practice") {
        val schedule = scheduleOf(classes(ClassesType.PRACTICE, start = LocalTime.of(8, 0)))

        When("the 5h reminder would fire at night (03:00)") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, setOf(ReminderKind.HOURS_BEFORE))

            Then("it is moved to 07:00") {
                reminders.single().triggerAtMillis shouldBe millis(date, LocalTime.of(7, 0))
            }
        }
    }

    Given("a practice that starts at 07:30") {
        val schedule = scheduleOf(classes(ClassesType.PRACTICE, start = LocalTime.of(7, 30)))

        When("moving the night reminder to 07:00 would leave less than 15 minutes") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, setOf(ReminderKind.HOURS_BEFORE))

            Then("07:00 is still fine (30 minutes before), so it is kept") {
                reminders.single().triggerAtMillis shouldBe millis(date, LocalTime.of(7, 0))
            }
        }
    }

    Given("a practice that starts at 07:10") {
        val schedule = scheduleOf(classes(ClassesType.PRACTICE, start = LocalTime.of(7, 10)))

        When("the shifted reminder would be too close to the start") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule), longBefore, zone, setOf(ReminderKind.HOURS_BEFORE))

            Then("it is dropped") {
                reminders.shouldBeEmpty()
            }
        }
    }

    Given("the same class present in two fetched weeks") {
        val schedule = scheduleOf(classes(ClassesType.LAB))

        When("planning") {
            val reminders = ClassReminderPlanner.plan(listOf(schedule, schedule), longBefore, zone, both)

            Then("duplicates are removed") {
                reminders shouldHaveSize 2
                reminders.first().id shouldNotBe reminders.last().id
            }
        }
    }

    Given("quiet hours shifting") {
        Then("late evening goes to 07:00 of the next day") {
            val shifted = ClassReminderPlanner.shiftOutOfQuietHours(ZonedDateTime.of(LocalDateTime.of(2026, 10, 13, 23, 30), zone))
            shifted shouldBe ZonedDateTime.of(LocalDateTime.of(2026, 10, 14, 7, 0), zone)
        }
        Then("daytime is left untouched") {
            val noon = ZonedDateTime.of(LocalDateTime.of(2026, 10, 13, 12, 0), zone)
            ClassReminderPlanner.shiftOutOfQuietHours(noon) shouldBe noon
        }
    }
})
