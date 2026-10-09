package kekmech.ru.feature_schedule_impl.data.network

import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_impl.data.model.GetSearchResultsResponse
import kekmech.ru.feature_schedule_impl.data.model.ScheduleDto

internal interface ScheduleService {

    suspend fun getSchedule(
        type: String,
        name: String,
        weekOffset: Int,
    ): ScheduleDto

    suspend fun getSearchResults(
        query: String,
        type: ScheduleType?,
    ): GetSearchResultsResponse
}
