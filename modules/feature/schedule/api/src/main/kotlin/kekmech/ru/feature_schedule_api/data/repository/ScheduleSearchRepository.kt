package kekmech.ru.feature_schedule_api.data.repository

import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult

interface ScheduleSearchRepository {

    suspend fun getSearchResults(
        query: String,
        type: ScheduleType? = null,
    ): Result<List<SearchResult>>
}
