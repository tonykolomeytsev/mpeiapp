package kekmech.ru.domain_schedule.dto

import java.io.Serializable

data class GetSearchResultsResponse(
    val items: List<SearchResult>
)

data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: SearchResultType
) : Serializable

enum class SearchResultType : Serializable { GROUP, PERSON }