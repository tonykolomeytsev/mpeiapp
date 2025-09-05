package kekmech.ru.feature_search.screens.main.elm

import kekmech.ru.feature_search.item.FilterItemType
import kekmech.ru.feature_search.item.FilterItemType.GROUPS
import kekmech.ru.feature_search.item.FilterItemType.MAP
import kekmech.ru.feature_search.item.FilterItemType.NOTES
import kekmech.ru.feature_search.item.FilterItemType.PERSONS
import kekmech.ru.feature_search.item.compareFilter
import kekmech.ru.feature_search.screens.main.elm.SearchEvent.Internal
import kekmech.ru.feature_search.screens.main.elm.SearchEvent.Ui
import kekmech.ru.feature_search.screens.main.simplify
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_search.screens.main.elm.SearchCommand as Command
import kekmech.ru.feature_search.screens.main.elm.SearchEffect as Effect
import kekmech.ru.feature_search.screens.main.elm.SearchEvent as Event
import kekmech.ru.feature_search.screens.main.elm.SearchState as State

internal class SearchReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) {
        when (event) {
            is Internal.SearchNotesSuccess -> state { copy(searchResultsNotes = event.results) }
            is Internal.SearchMapSuccess -> state { copy(searchResultsMap = event.results) }
            is Internal.SearchGroupsSuccess -> state { copy(searchResultsGroups = event.results) }
            is Internal.SearchPersonsSuccess -> state { copy(searchResultsPersons = event.results) }
        }
    }

    override fun Result.ui(event: Ui) {
        when (event) {
            is Ui.Init -> {
                effects {
                    +Effect.SetInitialQuery(state.query)
                        .takeIf { state.query.isNotEmpty() }
                }
                loadCommands(
                    simplifiedQuery = state.query.simplify(),
                    selectedFilter = state.selectedFilter,
                )
            }

            is Ui.Action.SearchContent -> {
                state { copy(query = event.query) }
                loadCommands(
                    simplifiedQuery = event.query.simplify(),
                    selectedFilter = state.selectedFilter,
                )
            }

            is Ui.Action.SelectFilter -> {
                val selectedFilter = event.filterItem.type
                state { copy(selectedFilter = selectedFilter) }
                loadCommands(
                    simplifiedQuery = state.query.simplify(),
                    selectedFilter = selectedFilter,
                )
            }
        }
    }

    private fun Result.loadCommands(
        simplifiedQuery: String,
        selectedFilter: FilterItemType,
    ) {
        if (simplifiedQuery.isEmpty()) return
        commands {
            +Command.SearchNotes(simplifiedQuery).takeIf { selectedFilter.compareFilter(NOTES) }
            +Command.SearchMap(simplifiedQuery).takeIf { selectedFilter.compareFilter(MAP) }
            +Command.SearchGroups(simplifiedQuery).takeIf { selectedFilter.compareFilter(GROUPS) }
            +Command.SearchPersons(simplifiedQuery).takeIf { selectedFilter.compareFilter(PERSONS) }
        }
    }
}
