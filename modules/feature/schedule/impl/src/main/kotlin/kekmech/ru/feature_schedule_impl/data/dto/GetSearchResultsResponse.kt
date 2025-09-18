package kekmech.ru.feature_schedule_impl.data.dto

import kekmech.ru.feature_schedule_impl.data.model.SearchResultDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GetSearchResultsResponse(
    @SerialName("items")
    val items: List<SearchResultDto>,
)


