package kekmech.ru.mock_server.routing.schedule

import android.content.res.AssetManager
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.mock_server.randomResponseDelay
import kekmech.ru.mock_server.routing.schedule.model.ScheduleTypeDto
import kekmech.ru.mock_server.routing.schedule.model.SearchResultDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// The real backend returns at most 30 results
private const val MaxSearchResults = 30

/**
 * Search of the schedules, as the real backend does it: the query is a case-insensitive part
 * of the name, and the results of all types are returned when the type is not given.
 *
 * The results are taken from `assets/schedule/search.json`, collected from the real backend:
 * the groups for the queries "С-12" and "Сэ-12", the teachers for "Куликова", "Адамов" and "Иван".
 * The only made-up one is the teacher "Бободжанов Абдухафиз Абдурасулович".
 */
internal fun Routing.getSearchResultsV1(assetManager: AssetManager) {
    val searchResults by lazy { loadSearchResults(assetManager) }
    get("/schedule/v1/search") {
        randomResponseDelay()
        val query = call.request.queryParameters["q"].orEmpty()
        val type = call.request.queryParameters["type"]?.let(ScheduleTypeDto::valueOf)
        val items = searchResults
            .filter { type == null || it.type == type }
            .filter { it.name.contains(query, ignoreCase = true) }
            .take(MaxSearchResults)
        call.respond(GetSearchResultsResponse(items))
    }
}

@Serializable
internal data class GetSearchResultsResponse(
    @SerialName("items")
    val items: List<SearchResultDto>,
)

private fun loadSearchResults(assetManager: AssetManager): List<SearchResultDto> =
    assetManager.open("schedule/search.json").bufferedReader().use { reader ->
        Json.decodeFromString<GetSearchResultsResponse>(reader.readText()).items
    }
