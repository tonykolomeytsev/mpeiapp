package kekmech.ru.feature_search_impl.screens.schedule_details.elm

import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class ScheduleDetailsStoreFactory(
    private val actor: ScheduleDetailsActor,
) {

    fun create(searchResult: SearchResult) = ElmStoreCompat(
        initialState = ScheduleDetailsState(searchResult),
        reducer = ScheduleDetailsReducer(),
        actor = actor,
        startEvent = ScheduleDetailsEvent.Ui.Init,
    )
}
