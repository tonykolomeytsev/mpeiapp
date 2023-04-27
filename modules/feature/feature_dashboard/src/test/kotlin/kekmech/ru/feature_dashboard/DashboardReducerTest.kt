package kekmech.ru.feature_dashboard

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardCommand
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardEvent.Ui
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardReducer
import kekmech.ru.feature_dashboard.screens.main.elm.DashboardState

internal class DashboardReducerTest : BehaviorSpec({
    val reducer = DashboardReducer()

    Given("Initial state") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    DashboardCommand.GetSelectedGroupName,
                    DashboardCommand.LoadNotes,
                    DashboardCommand.LoadSchedule(0),
                    DashboardCommand.LoadSchedule(1),
                    DashboardCommand.LoadFavoriteSchedules,
                    DashboardCommand.LoadSession,
                )
            }
        }
    }
}) {
    private companion object {
        private val STATE = DashboardState()
    }
}
