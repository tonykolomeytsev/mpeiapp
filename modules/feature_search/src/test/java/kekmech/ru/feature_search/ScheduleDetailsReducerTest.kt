package kekmech.ru.feature_search

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule.dto.SearchResultType
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsAction
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsEvent.Wish
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsReducer
import kekmech.ru.feature_search.schedule_details.elm.ScheduleDetailsState

class ScheduleDetailsReducerTest : BehaviorSpec({
    val reducer = ScheduleDetailsReducer()
    Given("Initial state") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Wish.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldContainExactly(
                    ScheduleDetailsAction.LoadSchedule(SEARCH_RESULT.name, 0),
                    ScheduleDetailsAction.LoadSchedule(SEARCH_RESULT.name, 1),
                    ScheduleDetailsAction.LoadFavorites
                )
            }
        }
    }
}) {
    private companion object {
        private val SEARCH_RESULT = SearchResult(
            id = "id",
            name = "Зуев Юрий Юрьевич",
            description = "",
            type = SearchResultType.PERSON
        )
        private val STATE = ScheduleDetailsState(SEARCH_RESULT)
    }
}