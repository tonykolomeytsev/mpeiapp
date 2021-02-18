package kekmech.ru.feature_schedule.find_schedule.elm

import kekmech.ru.domain_schedule.dto.SearchResult

internal data class FindScheduleState(
    val continueTo: String,
    val isLoading: Boolean = false,
    val isContinueButtonEnabled: Boolean = false,
    val selectScheduleAfterSuccess: Boolean,
    val searchResults: List<SearchResult> = emptyList()
)

internal sealed class FindScheduleEvent {

    sealed class Wish : FindScheduleEvent() {

        object Init : Wish()

        object Click {
            data class Continue(val scheduleName: String) : Wish()
        }

        object Action {
            data class GroupNumberChanged(val scheduleName: String) : Wish()
        }
    }

    sealed class News : FindScheduleEvent() {
        data class GroupLoadedSuccessfully(val scheduleName: String) : News()
        data class GroupLoadingError(val throwable: Throwable) : News()
        data class SearchResultsLoaded(val results: List<SearchResult>) : News()
    }
}

internal sealed class FindScheduleEffect {
    object ShowError : FindScheduleEffect()
    object ShowSomethingWentWrongError : FindScheduleEffect()
    data class NavigateNextFragment(val continueTo: String, val groupName: String) : FindScheduleEffect()
}

internal sealed class FindScheduleAction {
    data class FindGroup(val scheduleName: String) : FindScheduleAction()
    data class SelectGroup(val scheduleName: String) : FindScheduleAction()
    data class SearchForAutocomplete(val query: String) : FindScheduleAction()
}