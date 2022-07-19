package kekmech.ru.feature_dashboard

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import kekmech.ru.feature_dashboard.elm.DashboardAction
import kekmech.ru.feature_dashboard.elm.DashboardEvent.Wish
import kekmech.ru.feature_dashboard.elm.DashboardReducer
import kekmech.ru.feature_dashboard.elm.DashboardState

class DashboardReducerTest : BehaviorSpec({
    val reducer = DashboardReducer()

    Given("Initial state") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state.isLoading.shouldBeTrue()
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    DashboardAction.GetSelectedGroupName,
                    DashboardAction.LoadNotes,
                    DashboardAction.LoadSchedule(0),
                    DashboardAction.LoadSchedule(1),
                    DashboardAction.LoadFavoriteSchedules,
                    DashboardAction.LoadSession
                )
            }
        }
    }
}) {
    private companion object {
        private val STATE = DashboardState()
    }
}