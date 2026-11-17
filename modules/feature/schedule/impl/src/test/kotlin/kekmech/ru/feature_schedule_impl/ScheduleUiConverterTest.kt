package kekmech.ru.feature_schedule_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_schedule_api.domain.model.Schedule
import kekmech.ru.feature_schedule_api.domain.model.ScheduleKey
import kekmech.ru.feature_schedule_api.domain.model.ScheduleRefreshState
import kekmech.ru.feature_schedule_api.domain.model.ScheduleResource
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_impl.presentation.screen.main.SavedScheduleBannerUi
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleFabUi
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleHeaderFormatter
import kekmech.ru.feature_schedule_impl.presentation.screen.main.ScheduleUiConverter
import kekmech.ru.feature_schedule_impl.presentation.screen.main.elm.ScheduleState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class ScheduleUiConverterTest : BehaviorSpec({
    Given("Header formatter") {
        When("Format the day") {
            val title = ScheduleHeaderFormatter.formatDay(TUESDAY, DAYS_OF_WEEK, MONTHS)
            Then("Check day of week, day of month and month in the genitive case") {
                title shouldBe "Вторник, 10 ноября"
            }
        }
        When("Format the time of the saved schedule") {
            val updatedAt = ScheduleHeaderFormatter.formatUpdatedAt(
                updatedAt = Instant.parse("2026-11-10T11:20:00Z"),
                locale = Locale.forLanguageTag("ru-RU"),
                zoneId = ZoneId.of("Europe/Moscow"),
            )
            Then("Check it's in the local time zone") {
                updatedAt shouldBe "10 ноября, 14:20"
            }
        }
    }

    Given("Schedule state on Tuesday of the current week") {
        val state = ScheduleState(appSettings = APP_SETTINGS, selectedDate = TUESDAY)
        When("Map state") {
            val ui = ScheduleUiConverter.map(state)
            Then("Check the selected day is Tuesday") {
                ui.selectedDayOfWeek shouldBe 2
            }
            Then("Check the pages of the current week and its neighbours") {
                ui.dayPages.keys shouldBe setOf(-1, 0, 1)
            }
            Then("Check the FAB leads to the next week") {
                ui.fab shouldBe ScheduleFabUi(isNextWeek = true, isVisible = true)
            }
            Then("Check the saved schedule banner is hidden") {
                ui.savedScheduleBanner.shouldBeNull()
            }
        }
        When("Map state of another week with the FAB hidden by the scroll") {
            val ui = ScheduleUiConverter.map(state.copy(weekOffset = 2, isNavigationFabVisible = false))
            Then("Check the FAB leads back to the current week and is hidden") {
                ui.fab shouldBe ScheduleFabUi(isNextWeek = false, isVisible = false)
            }
        }
        When("Map state with the navigation button turned off in the settings") {
            val ui = ScheduleUiConverter.map(
                state.copy(appSettings = APP_SETTINGS.copy(showNavigationButton = false)),
            )
            Then("Check there is no FAB") {
                ui.fab.shouldBeNull()
            }
        }
        When("Map state with the saved schedule which failed to update") {
            val ui = ScheduleUiConverter.map(state.copy(schedule = mapOf(0 to FAILED_REFRESH_RESOURCE)))
            Then("Check the banner shows when the schedule was downloaded") {
                ui.savedScheduleBanner shouldBe SavedScheduleBannerUi(updatedAt = UPDATED_AT)
            }
        }
    }
}) {

    private companion object {
        val TUESDAY: LocalDate = LocalDate.of(2026, 11, 10)
        val UPDATED_AT: Instant = Instant.parse("2026-11-09T10:15:30Z")

        val DAYS_OF_WEEK = arrayOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")
        val MONTHS = arrayOf(
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря",
        )

        val APP_SETTINGS = AppSettings(
            isDarkThemeEnabled = false,
            appTheme = AppTheme.System,
            autoHideBottomSheet = false,
            isSnowEnabled = false,
            languageCode = "ru_RU",
            showNavigationButton = true,
            mapAppearanceType = "hybrid",
        )

        val FAILED_REFRESH_RESOURCE = ScheduleResource(
            key = ScheduleKey(ScheduleType.GROUP, "C-12-16", TUESDAY.minusDays(1)),
            value = Schedule(name = "C-12-16", id = "12345", type = ScheduleType.GROUP, weeks = emptyList()),
            updatedAt = UPDATED_AT,
            refresh = ScheduleRefreshState.Failed(IllegalStateException(), UPDATED_AT),
        )
    }
}
