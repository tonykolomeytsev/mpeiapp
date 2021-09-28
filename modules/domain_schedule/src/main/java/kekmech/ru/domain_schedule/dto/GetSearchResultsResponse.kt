package kekmech.ru.domain_schedule.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class GetSearchResultsResponse(
    val items: List<SearchResult>
)

@Parcelize
data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: SearchResultType
) : Parcelable

@Parcelize
enum class SearchResultType : Parcelable { GROUP, PERSON }