package kekmech.ru.feature_search_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_ui.compose.ParallelClassesUi
import kekmech.ru.feature_search_impl.screens.schedule_details.ClassesCountForm
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsUiConverter
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class ScheduleDetailsUiConverterTest : BehaviorSpec({
    Given("Schedule is loading") {
        val state = ScheduleDetailsState(SEARCH_RESULT, thisWeek = week(THIS_MONDAY))
        When("Map state") {
            val ui = ScheduleDetailsUiConverter.map(state)
            Then("Check header is shown right away") {
                ui.name shouldBe SEARCH_RESULT.name
                ui.description shouldBe SEARCH_RESULT.description
            }
            Then("Check favorites and weeks are loading") {
                ui.isInFavorites.shouldBeNull()
                ui.weeks.shouldBeNull()
            }
        }
    }
    Given("Both weeks are loaded and Monday with parallel labs is selected") {
        val state = ScheduleDetailsState(
            searchResult = SEARCH_RESULT,
            thisWeek = week(THIS_MONDAY, mondayClasses = listOf(LECTURE, LAB_1, LAB_2)),
            nextWeek = week(NEXT_MONDAY),
            isInFavorites = true,
            selectedDayDate = THIS_MONDAY,
        )
        When("Map state") {
            val ui = ScheduleDetailsUiConverter.map(state)
            val weeks = ui.weeks.shouldNotBeNull()
            Then("Check favorites") {
                ui.isInFavorites shouldBe true
            }
            Then("Check classes counts of each week") {
                weeks.thisWeekClassesCount shouldBe 3
                weeks.nextWeekClassesCount shouldBe 0
            }
            Then("Check days of both weeks, including Sundays, with Monday selected") {
                weeks.days shouldHaveSize 14
                weeks.days.first().date shouldBe THIS_MONDAY
                weeks.days.last().date shouldBe NEXT_MONDAY.plusDays(6)
                weeks.days.filter { it.isSelected }.map { it.date } shouldContainExactly listOf(THIS_MONDAY)
            }
            Then("Check parallel labs are merged into a single card") {
                weeks.selectedDayClasses shouldHaveSize 2
                weeks.selectedDayClasses[0].shouldBeInstanceOf<ParallelClassesUi.Single>()
                weeks.selectedDayClasses[1].shouldBeInstanceOf<ParallelClassesUi.SameNameAndType>()
            }
        }
    }
    Given("Next week failed to load and the selected day has no classes") {
        val state = ScheduleDetailsState(
            searchResult = SEARCH_RESULT,
            thisWeek = week(THIS_MONDAY, mondayClasses = listOf(LECTURE)),
            nextWeek = emptyList(),
            selectedDayDate = THIS_MONDAY.plusDays(1),
        )
        When("Map state") {
            val weeks = ScheduleDetailsUiConverter.map(state).weeks.shouldNotBeNull()
            Then("Check only this week is shown") {
                weeks.thisWeekClassesCount shouldBe 1
                weeks.nextWeekClassesCount shouldBe 0
                weeks.days shouldHaveSize 7
            }
            Then("Check selected day has no classes") {
                weeks.selectedDayClasses.shouldBeEmpty()
            }
        }
    }
    Given("Classes counts") {
        When("Pick the grammatical form") {
            Then("Check 1, 21, 101 classes") {
                listOf(1, 21, 101).map(ScheduleDetailsUiConverter::classesCountForm) shouldBe
                    List(3) { ClassesCountForm.One }
            }
            Then("Check 2, 3, 4, 22 classes") {
                listOf(2, 3, 4, 22).map(ScheduleDetailsUiConverter::classesCountForm) shouldBe
                    List(4) { ClassesCountForm.Few }
            }
            Then("Check 0, 5, 11, 12, 14, 19, 20, 111 classes") {
                listOf(0, 5, 11, 12, 14, 19, 20, 111).map(ScheduleDetailsUiConverter::classesCountForm) shouldBe
                    List(8) { ClassesCountForm.Many }
            }
        }
    }
}) {

    private companion object {

        val SEARCH_RESULT = SearchResult(
            id = "id",
            name = "С-12-16",
            description = "ИЭТЭ, 3 курс",
            type = ScheduleType.GROUP,
        )
        val THIS_MONDAY: LocalDate = LocalDate.of(2026, Month.NOVEMBER, 9)
        val NEXT_MONDAY: LocalDate = THIS_MONDAY.plusWeeks(1)

        val LECTURE = Classes(
            name = "Электротехника",
            type = ClassesType.LECTURE,
            rawType = null,
            place = "А-300",
            groups = "С-12-16",
            person = "Зуев Ю.Ю.",
            time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)),
            number = 1,
        )
        val LAB_1 = LECTURE.copy(
            name = "Физика",
            type = ClassesType.LAB,
            place = "Е-306",
            groups = "С-12-16\\1п",
            number = 2,
            time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
        )
        val LAB_2 = LAB_1.copy(place = "Е-308", groups = "С-12-16\\2п")

        /**
         * Seven days of the week, like `ScheduleDetailsReducer.mapToDays()` makes them
         */
        fun week(monday: LocalDate, mondayClasses: List<Classes> = emptyList()): List<Day> =
            (1..7).map { dayOfWeek ->
                Day(
                    dayOfWeek = dayOfWeek,
                    date = monday.plusDays(dayOfWeek - 1L),
                    classes = if (dayOfWeek == 1) mondayClasses else emptyList(),
                )
            }
    }
}
