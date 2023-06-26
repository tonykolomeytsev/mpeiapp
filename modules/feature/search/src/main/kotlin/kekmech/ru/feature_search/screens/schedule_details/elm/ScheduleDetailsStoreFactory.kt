package kekmech.ru.feature_search.screens.schedule_details.elm

import kekmech.ru.domain_schedule.dto.SearchResult
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