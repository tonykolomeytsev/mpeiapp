package kekmech.ru.feature_search_impl

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kekmech.ru.feature_map_api.domain.model.Location
import kekmech.ru.feature_map_api.domain.model.MapMarker
import kekmech.ru.feature_map_api.domain.model.MarkerType
import kekmech.ru.feature_notes_api.domain.model.Note
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_search_impl.item.FilterItem
import kekmech.ru.feature_search_impl.item.FilterItemType
import kekmech.ru.feature_search_impl.screens.main.elm.SearchCommand
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEffect
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent.Internal
import kekmech.ru.feature_search_impl.screens.main.elm.SearchEvent.Ui
import kekmech.ru.feature_search_impl.screens.main.elm.SearchReducer
import kekmech.ru.feature_search_impl.screens.main.elm.SearchState
import kekmech.ru.feature_search_impl.screens.main.simplify
import java.time.LocalDateTime

class SearchReducerTest : BehaviorSpec({
    val reducer = SearchReducer()
    Given("Initial state (empty query, filter all)") {
        val givenState = STATE
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
    Given("Initial state (non-empty query, filter all)") {
        val givenState = STATE.copy(query = QUERY_NON_EMPTY)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldContainExactly(SearchEffect.SetInitialQuery(QUERY_NON_EMPTY))
            }
            Then("Check actions") {
                val simplifiedQuery = QUERY_NON_EMPTY.simplify()
                actions.shouldContainExactly(
                    SearchCommand.SearchNotes(simplifiedQuery),
                    SearchCommand.SearchMap(simplifiedQuery),
                    SearchCommand.SearchGroups(simplifiedQuery),
                    SearchCommand.SearchPersons(simplifiedQuery)
                )
            }
        }
    }
    Given("Initial state (non-empty query, filter groups)") {
        val givenState = STATE.copy(query = QUERY_NON_EMPTY, selectedFilter = FilterItemType.GROUPS)
        When("Wish.Init") {
            val (state, effects, actions) = reducer.reduce(Ui.Init, givenState)
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldContainExactly(SearchEffect.SetInitialQuery(QUERY_NON_EMPTY))
            }
            Then("Check actions") {
                val simplifiedQuery = QUERY_NON_EMPTY.simplify()
                actions.shouldContainExactly(
                    SearchCommand.SearchGroups(simplifiedQuery)
                )
            }
        }
    }
    Given("User interactions (non-empty query, filter persons)") {
        val givenState =
            STATE.copy(query = QUERY_NON_EMPTY, selectedFilter = FilterItemType.PERSONS)
        When("Wish.Action.SearchContent") {
            val (state, effects, actions) = reducer.reduce(
                Ui.Action.SearchContent(QUERY_NON_EMPTY),
                givenState
            )
            Then("Check state") {
                state shouldBe givenState
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                val simplifiedQuery = QUERY_NON_EMPTY.simplify()
                actions.shouldContainExactly(
                    SearchCommand.SearchPersons(simplifiedQuery)
                )
            }
        }
        When("Wish.Action.SelectFilter (non-empty query)") {
            val (state, effects, actions) = reducer.reduce(
                Ui.Action.SelectFilter(FILTER_ITEM_MAP),
                givenState
            )
            Then("Check state") {
                state.selectedFilter shouldBe FilterItemType.MAP
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                val simplifiedQuery = QUERY_NON_EMPTY.simplify()
                actions.shouldContainExactly(
                    SearchCommand.SearchMap(simplifiedQuery)
                )
            }
        }
    }
    Given("After results loading success") {
        val givenState = STATE.copy(query = QUERY_NON_EMPTY)
        When("News.SearchNotesSuccess") {
            val (state, effects, actions) = reducer.reduce(
                Internal.SearchNotesSuccess(NOTES_RESULTS),
                givenState
            )
            Then("Check state") {
                state.searchResultsNotes.shouldContainExactly(NOTES_RESULTS)
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.SearchMapSuccess") {
            val (state, effects, actions) = reducer.reduce(
                Internal.SearchMapSuccess(MAP_RESULTS),
                givenState
            )
            Then("Check state") {
                state.searchResultsMap.shouldContainExactly(MAP_RESULTS)
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.SearchGroupsSuccess") {
            val (state, effects, actions) = reducer.reduce(
                Internal.SearchGroupsSuccess(
                    GROUP_RESULTS
                ), givenState
            )
            Then("Check state") {
                state.searchResultsGroups.shouldContainExactly(GROUP_RESULTS)
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
        When("News.SearchPersonsSuccess") {
            val (state, effects, actions) = reducer.reduce(
                Internal.SearchPersonsSuccess(
                    PERSON_RESULTS
                ), givenState
            )
            Then("Check state") {
                state.searchResultsPersons.shouldContainExactly(PERSON_RESULTS)
            }
            Then("Check effects") {
                effects.shouldBeEmpty()
            }
            Then("Check actions") {
                actions.shouldBeEmpty()
            }
        }
    }
}) {

    private companion object {

        private const val QUERY_EMPTY = ""
        private const val QUERY_NON_EMPTY = "Кек)))"
        private val STATE = SearchState(query = QUERY_EMPTY)
        private val FILTER_ITEM_MAP = FilterItem(FilterItemType.MAP, false)
        private val NOTES_RESULTS = listOf(Note("", LocalDateTime.now(), "", target = 0))
        private val MAP_RESULTS = listOf(
            MapMarker("", "", Location(0.0, 0.0), "", MarkerType.OTHER, "", "")
        )
        private val PERSON_RESULTS = listOf(
            SearchResult("", "Зуев Юрий Юрьевич", "", ScheduleType.PERSON)
        )
        private val GROUP_RESULTS = listOf(
            SearchResult("", "С-06-18", "", ScheduleType.GROUP)
        )
    }
}
