package kekmech.ru.feature_schedule_impl.data.dto

import androidx.annotation.Keep
import kekmech.ru.feature_schedule_api.domain.model.SearchResult

@Keep
internal data class GetSearchResultsResponse(
    val items: List<SearchResult>,
)


