package kekmech.ru.feature_search_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_ui.compose.ParallelClassesUi
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayItem
import kekmech.ru.feature_schedule_ui.compose.ScheduleDayPagesConverter
import kekmech.ru.feature_search_impl.screens.schedule_details.ClassesCountForm
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsFavoritesFabUi
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsUiConverter
import kekmech.ru.feature_search_impl.screens.schedule_details.ScheduleDetailsWeeksSummaryUi
import kekmech.ru.feature_search_impl.screens.schedule_details.elm.ScheduleDetailsState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class ScheduleDetailsUiConverterTest : BehaviorSpec({
    Given("Next week is loading") {
        val state = ScheduleDetailsState(
            searchResult = SEARCH_RESULT,
            schedule = mapOf(0 to resource(THIS_MONDAY), 1 to loadingResource(NEXT_MONDAY)),
            selectedDate = THIS_MONDAY,
        )
        When("Map state") {
            val ui = ScheduleDetailsUiConverter.map(state)
            Then("Check the name is shown right away") {
                ui.name shouldBe SEARCH_RESULT.name
            }
            Then("Check favorites and the weeks summary are loading") {
                ui.favoritesFab.shouldBeNull()
                ui.weeksSummary.shouldBeNull()
            }
            Then("Check the next week is shown as loading") {
                ui.dayPages.getValue(1) shouldBe ScheduleDayPagesConverter.loadingPages
            }
        }
    }
    Given("Both weeks are loaded and Monday with parallel labs is selected") {
        val state = ScheduleDetailsState(
            searchResult = SEARCH_RESULT,
            schedule = mapOf(
                0 to resource(THIS_MONDAY, mondayClasses = listOf(LECTURE, LAB_1, LAB_2)),
                1 to resource(NEXT_MONDAY),
            ),
            selectedDate = THIS_MONDAY,
            isInFavorites = true,
        )
        When("Map state") {
            val ui = ScheduleDetailsUiConverter.map(state)
            Then("Check the favorites button removes the schedule from favorites") {
                ui.favoritesFab shouldBe ScheduleDetailsFavoritesFabUi(isInFavorites = true, isVisible = true)
            }
            Then("Check classes counts of each week") {
                ui.weeksSummary shouldBe ScheduleDetailsWeeksSummaryUi(thisWeekClassesCount = 3, nextWeekClassesCount = 0)
            }
            Then("Check the pages of the selected week and its neighbours") {
                ui.dayPages.keys shouldBe setOf(-1, 0, 1)
                ui.dayPages.getValue(0) shouldHaveSize 6
            }
            Then("Check parallel labs are merged into a single card") {
                val classes = ui.dayPages.getValue(0).first().items.filterIsInstance<ScheduleDayItem.ClassesEntry>()
                classes shouldHaveSize 2
                classes[0].model.shouldBeInstanceOf<ParallelClassesUi.Single>()
                classes[1].model.shouldBeInstanceOf<ParallelClassesUi.SameNameAndType>()
            }
            Then("Check the day without classes is the self study day") {
                ui.dayPages.getValue(0)[1].items shouldBe listOf(ScheduleDayItem.SelfStudy)
            }
        }
    }
    Given("Next week failed to load and the classes are scrolled down") {
        val state = ScheduleDetailsState(
            searchResult = SEARCH_RESULT,
            schedule = mapOf(0 to resource(THIS_MONDAY, mondayClasses = listOf(LECTURE))),
            observeErrors = mapOf(1 to ERROR),
            selectedDate = THIS_MONDAY,
            isInFavorites = false,
            isFavoritesFabVisible = false,
        )
        When("Map state") {
            val ui = ScheduleDetailsUiConverter.map(state)
            Then("Check the failed week has no classes in the summary") {
                ui.weeksSummary shouldBe ScheduleDetailsWeeksSummaryUi(thisWeekClassesCount = 1, nextWeekClassesCount = 0)
            }
            Then("Check the failed week shows the error") {
                ui.dayPages.getValue(1).first().items shouldBe listOf(ScheduleDayItem.Error(ERROR))
            }
            Then("Check the favorites button is hidden") {
                ui.favoritesFab.shouldNotBeNull().isVisible shouldBe false
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
        val UPDATED_AT: Instant = Instant.parse("2026-11-09T10:15:30Z")
        val ERROR = IllegalStateException()

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

        fun resource(monday: LocalDate, mondayClasses: List<Classes> = emptyList()) = ScheduleResource(
            key = ScheduleKey(ScheduleType.GROUP, SEARCH_RESULT.name, monday),
            value = Schedule(
                name = SEARCH_RESULT.name,
                id = "12345",
                type = ScheduleType.GROUP,
                weeks = listOf(
                    Week(
                        weekOfSemester = WeekOfSemester.Studying(12),
                        weekOfYear = 46,
                        firstDayOfWeek = monday,
                        days = listOf(Day(dayOfWeek = 1, date = monday, classes = mondayClasses)),
                    ),
                ),
            ),
            updatedAt = UPDATED_AT,
            refresh = ScheduleRefreshState.Idle,
        )

        /** Never downloaded and the download is in progress */
        fun loadingResource(monday: LocalDate) = ScheduleResource(
            key = ScheduleKey(ScheduleType.GROUP, SEARCH_RESULT.name, monday),
            value = null,
            updatedAt = null,
            refresh = ScheduleRefreshState.InProgress,
        )
    }
}
