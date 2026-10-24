package kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeCommand as Command
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Internal
import kekmech.ru.feature_bars_impl.presentation.screen.main_compose.elm.BarsComposeEvent.Ui

class BarsComposeReducerTest : BehaviorSpec({
    val reducer = BarsComposeReducer()

    Given("Initial state") {
        val initial = BarsComposeState()

        Then("Disciplines tab is selected") {
            initial.selectedTab shouldBe BarsTab.DISCIPLINES
        }

        When("Ui.Click.Tab(SUMMARY)") {
            val (state, _, commands) = reducer.reduce(Ui.Click.Tab(BarsTab.SUMMARY), initial)

            Then("Summary tab is selected, nothing is reloaded") {
                state.selectedTab shouldBe BarsTab.SUMMARY
                commands shouldBe emptyList<Command>()
            }
        }

        When("Ui.Click.SummaryDiscipline twice") {
            val (expanded, _, _) = reducer.reduce(Ui.Click.SummaryDiscipline("Физика"), initial)
            val (collapsed, _, _) = reducer.reduce(Ui.Click.SummaryDiscipline("Физика"), expanded)

            Then("the discipline is expanded, then collapsed") {
                expanded.expandedSummary shouldBe setOf("Физика")
                collapsed.expandedSummary shouldBe emptySet()
            }
        }

        When("Internal.GetCurrentWeekSuccess") {
            val (state, _, _) = reducer.reduce(Internal.GetCurrentWeekSuccess(7), initial)

            Then("the week is stored") {
                state.currentWeek shouldBe 7
            }
        }

        When("Internal.SubscribeAuthStatusSuccess(LoggedIn)") {
            val (_, _, commands) = reducer.reduce(
                Internal.SubscribeAuthStatusSuccess(AuthStatus.LoggedIn),
                initial,
            )

            Then("profile, marks and the current week are requested") {
                commands.shouldContainExactly(
                    listOf(Command.GetProfile, Command.GetMarks, Command.GetCurrentWeek)
                )
            }
        }
    }
})
