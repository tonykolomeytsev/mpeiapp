package kekmech.ru.feature_schedule_impl.data.dto

import kekmech.ru.feature_schedule_api.domain.model.SearchResult

internal data class GetSearchResultsResponse(
    val items: List<SearchResult>,
)


