package kekmech.ru.feature_search.schedule_details.elm

import kekmech.ru.domain_schedule.dto.SearchResult
import vivid.money.elmslie.core.store.ElmStore

internal class ScheduleDetailsFeatureFactory(
    private val actor: ScheduleDetailsActor
) {

    fun create(searchResult: SearchResult) = ElmStore(
        initialState = ScheduleDetailsState(searchResult),
        reducer = ScheduleDetailsReducer(),
        actor = actor
    )
}
