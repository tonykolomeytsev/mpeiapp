package kekmech.ru.domain_schedule.repository

import io.reactivex.rxjava3.core.Single
import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule.network.ScheduleService
import kekmech.ru.domain_schedule_models.dto.ScheduleType

public class ScheduleSearchRepository(
    private val scheduleService: ScheduleService,
) {

    public fun getSearchResults(
        query: String,
        type: ScheduleType? = null,
    ): Single<GetSearchResultsResponse> =
        scheduleService.getSearchResults(query, type)
}
