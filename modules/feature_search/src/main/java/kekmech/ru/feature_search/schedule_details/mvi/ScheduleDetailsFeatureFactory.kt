package kekmech.ru.feature_search.schedule_details.mvi

import kekmech.ru.common_mvi.BaseFeature
import kekmech.ru.domain_schedule.dto.SearchResult

internal class ScheduleDetailsFeatureFactory(
    private val actor: ScheduleDetailsActor
) {

    fun create(searchResult: SearchResult) = BaseFeature(
        initialState = ScheduleDetailsState(searchResult),
        reducer = ScheduleDetailsReducer(),
        actor = actor
    ).start()
}