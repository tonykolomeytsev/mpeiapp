package kekmech.ru.domain_schedule.data

import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule.network.ScheduleService
import kekmech.ru.domain_schedule_models.dto.ScheduleType

class ScheduleSearchRepository(
    private val scheduleService: ScheduleService,
) {

    suspend fun getSearchResults(
        query: String,
        type: ScheduleType? = null,
    ): Result<GetSearchResultsResponse> =
        runCatching { scheduleService.getSearchResults(query, type) }
}
