package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.Time
import kekmech.ru.feature_schedule_api.domain.model.Week
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleClassesListConverter
import kekmech.ru.feature_schedule_impl.presentation.screen.main.compose.ScheduleDayItem
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import kekmech.ru.feature_schedule_ui.compose.ParallelClassesUi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

class ScheduleClassesListConverterTest : BehaviorSpec({
    Given("Day with parallel labs after the lunch and a window after them") {
        val state = stateWithClasses(listOf(CLASSES_1, CLASSES_2, LAB_1, LAB_2, CLASSES_5))
        When("Map state") {
            val items = ScheduleClassesListConverter.map(state).first().items
            Then("Check parallel labs are merged into a single card") {
                items.map { it::class } shouldBe listOf(
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.Lunch::class,
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.Window::class,
                    ScheduleDayItem.ClassesEntry::class,
                )
                val labs = items[3].shouldBeInstanceOf<ScheduleDayItem.ClassesEntry>()
                labs.classes shouldBe LAB_1
                val model = labs.model.shouldBeInstanceOf<ParallelClassesUi.SameNameAndType>()
                model.differentMetaInfo shouldHaveSize 2
            }
            Then("Check item keys are unique") {
                items.map { it.key }.distinct() shouldHaveSize items.size
            }
        }
    }
    Given("Day with parallel classes of different names") {
        val language = LAB_2.copy(name = "Foreign language", type = ClassesType.PRACTICE)
        val state = stateWithClasses(listOf(CLASSES_1, CLASSES_2, LAB_1, language))
        When("Map state") {
            val items = ScheduleClassesListConverter.map(state).first().items
            Then("Check they are separate cards without a window between them") {
                items.map { it::class } shouldBe listOf(
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.Lunch::class,
                    ScheduleDayItem.ClassesEntry::class,
                    ScheduleDayItem.ClassesEntry::class,
                )
                items.filterIsInstance<ScheduleDayItem.ClassesEntry>()
                    .drop(2)
                    .map { it.classes } shouldBe listOf(LAB_1, language)
            }
        }
    }
}) {

    private companion object {

        private val MONDAY = LocalDate.of(2020, Month.SEPTEMBER, 14)
        private val APP_SETTINGS = AppSettings(
            isDarkThemeEnabled = false,
            appTheme = AppTheme.System,
            autoHideBottomSheet = false,
            isSnowEnabled = false,
            languageCode = "ru_RU",
            showNavigationButton = false,
            mapAppearanceType = "hybrid",
        )
        private val CLASSES_1 = Classes(
            name = "Physics",
            type = ClassesType.LECTURE,
            rawType = null,
            place = "B-407",
            groups = "C-12-16",
            person = "Ivanov I.I.",
            time = Time(LocalTime.of(9, 20), LocalTime.of(10, 55)),
            number = 1,
        )
        private val CLASSES_2 = CLASSES_1.copy(
            name = "Metals",
            number = 2,
            time = Time(LocalTime.of(11, 10), LocalTime.of(12, 45)),
        )
        private val LAB_1 = CLASSES_1.copy(
            name = "Materials",
            type = ClassesType.LAB,
            place = "E-306",
            groups = "C-12-16\\1п",
            number = 3,
            time = Time(LocalTime.of(13, 45), LocalTime.of(15, 20)),
        )
        private val LAB_2 = LAB_1.copy(
            place = "E-308",
            groups = "C-12-16\\2п",
            person = "Petrov P.P.",
        )
        private val CLASSES_5 = CLASSES_1.copy(
            name = "Math",
            number = 5,
            time = Time(LocalTime.of(17, 20), LocalTime.of(18, 55)),
        )

        private fun stateWithClasses(classes: List<Classes>): ScheduleState {
            val schedule = Schedule(
                name = "C-12-16",
                id = "12345",
                type = ScheduleType.GROUP,
                weeks = listOf(
                    Week(
                        weekOfSemester = WeekOfSemester.Studying(3),
                        weekOfYear = 38,
                        firstDayOfWeek = MONDAY,
                        days = listOf(Day(dayOfWeek = 1, date = MONDAY, classes = classes)),
                    ),
                ),
            )
            val resource = ScheduleResource(
                key = ScheduleKey(ScheduleType.GROUP, "C-12-16", MONDAY),
                value = schedule,
                updatedAt = Instant.parse("2020-09-14T10:15:30Z"),
                refresh = ScheduleRefreshState.Idle,
            )
            return ScheduleState(
                appSettings = APP_SETTINGS,
                selectedDate = MONDAY,
                schedule = mapOf(0 to resource),
            )
        }
    }
}
