package kekmech.ru.feature_dashboard_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import kekmech.ru.feature_dashboard_impl.domain.model.UpcomingEventsPrediction
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardCommand
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEffect
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Internal
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardEvent.Ui
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard_impl.presentation.screen.main.elm.DashboardState
import kekmech.ru.feature_favorite_schedule_api.domain.model.FavoriteSchedule
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SelectedSchedule
import kekmech.ru.feature_schedule_api.domain.model.WeekOfSemester
import kekmech.ru.lib_elm.Resource
import kekmech.ru.lib_elm.toResource

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
                    DashboardCommand.ObserveSelectedSchedule,
                    DashboardCommand.ObserveSchedule,
                    DashboardCommand.GetActualNotes,
                    DashboardCommand.GetFavoriteSchedules,
                    DashboardCommand.GetWhatsNewBannerVisibility,
                )
            }
        }
        When("Internal.GetWhatsNewBannerVisibilitySuccess") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetWhatsNewBannerVisibilitySuccess(isVisible = true), givenState)
            Then("Show the banner") {
                state.isWhatsNewBannerVisible.shouldBeTrue()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.GetSelectedScheduleSuccess (another schedule)") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetSelectedScheduleSuccess(NewSelectedSchedule), givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.selectedSchedule.shouldBe(NewSelectedSchedule)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Reload notes of the new schedule") {
                commands.shouldContainExactly(DashboardCommand.GetActualNotes)
            }
        }
        When("Internal.GetSelectedScheduleSuccess (same schedule)") {
            val (state, effects, commands) = reducer
                .reduce(Internal.GetSelectedScheduleSuccess(InitialSelectedSchedule), givenState)
            Then("Check state") {
                state.selectedSchedule.shouldBe(InitialSelectedSchedule)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.ScheduleUpdated (success)") {
            val (state, effects, commands) = reducer.reduce(
                Internal.ScheduleUpdated(
                    weekOfSemester = NewWeekOfSemester.toResource(),
                    upcomingEvents = NewUpcomingEvents.toResource(),
                ),
                givenState,
            )
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.weekOfSemester.shouldBe(NewWeekOfSemester.toResource())
                state.upcomingEvents.shouldBe(NewUpcomingEvents.toResource())
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.ScheduleUpdated (failure)") {
            val (state, effects, commands) = reducer.reduce(
                Internal.ScheduleUpdated(
                    weekOfSemester = Resource.Error(RuntimeException()),
                    upcomingEvents = Resource.Error(RuntimeException()),
                ),
                givenState,
            )
            Then("Check state") {
                state.isLoading.shouldBeTrue()
                state.weekOfSemester.shouldBeTypeOf<Resource.Error>()
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
            Then("Show the refresh indicator") {
                state.isLoading.shouldBeTrue()
                state.shouldBe(givenState.copy(isRefreshingSchedule = true))
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Download the schedule") {
                commands.shouldContainExactly(
                    DashboardCommand.RefreshSchedule,
                    DashboardCommand.ObserveSchedule,
                    DashboardCommand.GetActualNotes,
                    DashboardCommand.GetFavoriteSchedules,
                )
            }
        }
        When("Ui.Action.SilentUpdate") {
            val (state, effects, commands) = reducer
                .reduce(Ui.Action.SilentUpdate, givenState)
            Then("No refresh indicator") {
                state.isLoading.shouldBeFalse()
                state.shouldBe(givenState)
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Don't force a download") {
                commands.shouldContainExactly(
                    DashboardCommand.ObserveSchedule,
                    DashboardCommand.GetActualNotes,
                    DashboardCommand.GetFavoriteSchedules,
                )
            }
        }
    }
    Given("Pulled to refresh with the schedule shown") {
        val givenState =
            DashboardState(
                selectedSchedule = NewSelectedSchedule,
                weekOfSemester = NewWeekOfSemester.toResource(),
                upcomingEvents = NewUpcomingEvents.toResource(),
                actualNotes = NewActualNotes.toResource(),
                favoriteSchedules = NewFavoriteSchedules.toResource(),
                isRefreshingSchedule = true,
            )

        When("Internal.RefreshScheduleFinished (success)") {
            val (state, effects, commands) = reducer
                .reduce(Internal.RefreshScheduleFinished(error = null), givenState)
            Then("Hide the refresh indicator") {
                state.isLoading.shouldBeFalse()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
        When("Internal.RefreshScheduleFinished (failure)") {
            val (state, effects, commands) = reducer
                .reduce(Internal.RefreshScheduleFinished(RuntimeException()), givenState)
            Then("Hide the refresh indicator, keep the saved schedule") {
                state.isLoading.shouldBeFalse()
                state.upcomingEvents.shouldBe(NewUpcomingEvents.toResource())
            }
            Then("Tell that it couldn't be updated") {
                effects.shouldContainExactly(DashboardEffect.ShowLoadingError)
            }
            Then("No commands") {
                commands.shouldBeEmpty()
            }
        }
    }

    Given("The \"What's new\" banner is visible") {
        val givenState = STATE.copy(isWhatsNewBannerVisible = true)
        When("Ui.Click.WhatsNewBannerDismiss") {
            val (state, effects, commands) = reducer.reduce(Ui.Click.WhatsNewBannerDismiss, givenState)
            Then("Hide the banner") {
                state.isWhatsNewBannerVisible.shouldBeFalse()
            }
            Then("No effects") {
                effects.shouldBeEmpty()
            }
            Then("Remember that the banner was dismissed") {
                commands.shouldContainExactly(DashboardCommand.DismissWhatsNewBanner)
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
