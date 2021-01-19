package kekmech.ru.domain_schedule.dto

data class GetSearchResultsResponse(
    val items: List<SearchResult>
)

data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: SearchResultType
)

enum class SearchResultType { GROUP, PERSON }