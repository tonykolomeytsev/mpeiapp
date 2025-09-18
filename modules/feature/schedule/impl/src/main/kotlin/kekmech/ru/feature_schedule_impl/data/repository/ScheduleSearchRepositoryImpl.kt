package kekmech.ru.feature_schedule_impl.data.repository

import kekmech.ru.feature_schedule_api.data.repository.ScheduleSearchRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.feature_schedule_impl.data.mapper.SearchResultMapper
import kekmech.ru.feature_schedule_impl.data.network.ScheduleService

internal class ScheduleSearchRepositoryImpl(
    private val scheduleService: ScheduleService,
) : ScheduleSearchRepository {

    override suspend fun getSearchResults(
        query: String,
        type: ScheduleType?,
    ): Result<List<SearchResult>> =
        runCatching { scheduleService.getSearchResults(query, type).items }
            .map { SearchResultMapper.dtoToDomain(it) }
}
