package kekmech.ru.feature_schedule_impl.widget

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_impl.domain.ClassTimeline
import kekmech.ru.feature_schedule_impl.presentation.widget.WidgetDayPrefs
import java.time.LocalDate
import java.time.LocalTime

class ClassTimelineTest : BehaviorSpec({

    fun classes(name: String, start: String, end: String) = Classes(
        name = name,
        type = ClassesType.LECTURE,
        rawType = null,
        place = "Е-101",
        groups = "А-01-21",
        person = "Иванов И.И.",
        time = Time(LocalTime.parse(start), LocalTime.parse(end)),
        number = 1,
    )

    val first = classes("Физика", "09:20", "10:55")
    val second = classes("Матан", "11:10", "12:45")
    val day = listOf(second, first) // unsorted on purpose

    given("a day with two classes") {
        `when`("it is before the first one") {
            val timeline = ClassTimeline.of(day, LocalTime.of(8, 0))
            then("nothing is going on, the first one is next") {
                timeline.current shouldBe null
                timeline.next shouldBe first
                timeline.focus shouldBe first
                timeline.remaining shouldHaveSize 2
                timeline.changesAt shouldBe LocalTime.of(9, 20)
            }
        }
        `when`("the first one is going on") {
            val timeline = ClassTimeline.of(day, LocalTime.of(10, 0))
            then("it is current, the second one is next") {
                timeline.current shouldBe first
                timeline.next shouldBe second
                timeline.changesAt shouldBe LocalTime.of(10, 55)
            }
        }
        `when`("it is the break between the classes") {
            val timeline = ClassTimeline.of(day, LocalTime.of(11, 0))
            then("only the second one is left") {
                timeline.current shouldBe null
                timeline.focus shouldBe second
                timeline.remaining shouldBe listOf(second)
            }
        }
        `when`("the last one has just ended") {
            val timeline = ClassTimeline.of(day, LocalTime.of(12, 45))
            then("the day is over") {
                timeline.isOver shouldBe true
                timeline.focus shouldBe null
                timeline.changesAt shouldBe null
            }
        }
    }

    given("a day without classes") {
        val timeline = ClassTimeline.of(emptyList(), LocalTime.NOON)
        then("it is empty, not over") {
            timeline.isEmpty shouldBe true
            timeline.isOver shouldBe false
        }
    }

    given("the day switcher of the widget") {
        val today = LocalDate.of(2026, 10, 14) // Wednesday
        then("an offset chosen yesterday is forgotten") {
            WidgetDayPrefs.resolveOffset(3, today.minusDays(1).toEpochDay(), today) shouldBe 0
            WidgetDayPrefs.resolveOffset(3, today.toEpochDay(), today) shouldBe 3
        }
        then("it can not leave the loaded range") {
            WidgetDayPrefs.shiftOffset(0, 1) shouldBe 1
            WidgetDayPrefs.shiftOffset(0, -1) shouldBe -1
            WidgetDayPrefs.shiftOffset(28, 1) shouldBe 28
            WidgetDayPrefs.shiftOffset(-28, -1) shouldBe -28
        }
        then("the week offset follows the Monday of the week") {
            WidgetDayPrefs.weekOffset(today, today) shouldBe 0
            WidgetDayPrefs.weekOffset(today, LocalDate.of(2026, 10, 12)) shouldBe 0 // Monday of this week
            WidgetDayPrefs.weekOffset(today, LocalDate.of(2026, 10, 11)) shouldBe -1 // Sunday of the last week
            WidgetDayPrefs.weekOffset(today, LocalDate.of(2026, 10, 19)) shouldBe 1
        }
    }
})
