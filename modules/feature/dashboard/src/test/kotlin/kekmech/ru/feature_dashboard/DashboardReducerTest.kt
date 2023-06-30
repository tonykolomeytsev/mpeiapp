package kekmech.ru.feature_dashboard

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kekmech.ru.common_elm.Resource
import kekmech.ru.common_elm.toResource
import kekmech.ru.domain_dashboard.dto.UpcomingEventsPrediction
import kekmech.ru.domain_favorite_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_notes.dto.Note
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Ui
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState

internal class DashboardReducerTest : BehaviorSpec({
    val reducer = DashboardReducer()

    Given("Initial state") {
        val givenState = STATE
        When("Ui.Init") {
            val (state, effects, commands) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.selectedSchedule.shouldBe(InitialSelectedSchedule)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(
                    DashboardCommand.GetSelectedSchedule,
                    DashboardCommand.GetWeekOfSemester,
                    DashboardCommand.GetUpcomingEvents,
                    DashboardCommand.GetActualNotes,
                    DashboardCommand.GetFavoriteSchedules,
                )
            }
        }
        When("Internal.GetSelectedScheduleSuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetSelectedScheduleSuccess(NewSelectedSchedule), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.selectedSchedule.shouldBe(NewSelectedSchedule)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetWeekOfSemesterSuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetWeekOfSemesterSuccess(NewWeekOfSemester), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.weekOfSemester.shouldBe(NewWeekOfSemester.toResource())
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetWeekOfSemesterFailure") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetWeekOfSemesterFailure(RuntimeException()), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.weekOfSemester.shouldBeTypeOf<Resource.Error>()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetUpcomingEventsSuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetUpcomingEventsSuccess(NewUpcomingEvents), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.upcomingEvents.shouldBe(NewUpcomingEvents.toResource())
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetUpcomingEventsFailure") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetUpcomingEventsFailure(RuntimeException()), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.upcomingEvents.shouldBeTypeOf<Resource.Error>()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetActualNotesSuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetActualNotesSuccess(NewActualNotes), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.actualNotes.shouldBe(NewActualNotes.toResource())
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetActualNotesFailure") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetActualNotesFailure(RuntimeException()), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.actualNotes.shouldBeTypeOf<Resource.Error>()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetFavoriteSchedulesSuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetFavoriteSchedulesSuccess(NewFavoriteSchedules), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.favoriteSchedules.shouldBe(NewFavoriteSchedules.toResource())
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetFavoriteSchedulesFailure") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetFavoriteSchedulesFailure(RuntimeException()), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.favoriteSchedules.shouldBeTypeOf<Resource.Error>()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
    }
    Given("State after successful loading") {
        val givenState =
            DashboardState(
                selectedSchedule = NewSelectedSchedule,
                weekOfSemester = NewWeekOfSemester.toResource(),
                upcomingEvents = NewUpcomingEvents.toResource(),
                actualNotes = NewActualNotes.toResource(),
                favoriteSchedules = NewFavoriteSchedules.toResource(),
            )

        When("Ui.Action.SwipeToRefresh") {
            val (state, effects, commands) = reducer
                .reduce(Ui.Action.SwipeToRefresh, givenState)
            Then("Check state") {
                state.isLoading.shouldBeFalse()
                state.shouldBe(givenState)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Check commands") {
                commands.shouldContainExactly(
                    DashboardCommand.GetSelectedSchedule,
                    DashboardCommand.GetWeekOfSemester,
                    DashboardCommand.GetUpcomingEvents,
                    DashboardCommand.GetActualNotes,
                    DashboardCommand.GetFavoriteSchedules,
                )
            }
        }
    }
}) {

    private companion object {

        private val InitialSelectedSchedule =
            SelectedSchedule(name = "С-12-16", type = ScheduleType.GROUP)
        private val NewSelectedSchedule =
            SelectedSchedule(name = "Сэ-12-21", type = ScheduleType.GROUP)
        private val NewWeekOfSemester = WeekOfSemester.Studying(3)
        private val NewUpcomingEvents = UpcomingEventsPrediction.NoClassesNextWeek
        private val NewActualNotes = listOf<Note>()
        private val NewFavoriteSchedules = listOf<FavoriteSchedule>()
        private val STATE = DashboardState(
            selectedSchedule = InitialSelectedSchedule,
        )
    }
}
