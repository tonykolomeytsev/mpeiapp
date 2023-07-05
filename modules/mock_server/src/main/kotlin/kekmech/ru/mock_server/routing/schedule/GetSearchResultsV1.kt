package kekmech.ru.mock_server.routing.schedule

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_api.domain.model.SearchResult
import kekmech.ru.mock_server.randomResponseDelay

internal fun Routing.getSearchResultsV1() {
    get("/schedule/v1/search") {
        randomResponseDelay()
        val type = call.request.queryParameters["type"]
        call.respond(createSearchResults(type?.let(ScheduleType::valueOf)))
    }
}

@Suppress("unused")
private class GetSearchResultsResponse(
     val items: List<SearchResult>,
)

private fun createSearchResults(type: ScheduleType?): GetSearchResultsResponse =
    GetSearchResultsResponse(
        items = listOfNotNull(
            SearchResult(
                id = "12345",
                name = "Сэ-12-21",
                description = "Институт энергомашиностроения и механики | Очная",
                type = ScheduleType.GROUP,
            ).takeIf { type == ScheduleType.GROUP },
            SearchResult(
                id = "123456",
                name = "С-12-16",
                description = "Давно выпустившаяся группа",
                type = ScheduleType.GROUP,
            ).takeIf { type == ScheduleType.GROUP },
            SearchResult(
                id = "1234567",
                name = "Бободжанов Абдухафиз Абдурасулович",
                description = "Самый добрый преподаватель по вышмату",
                type = ScheduleType.PERSON,
            ).takeIf { type == ScheduleType.PERSON },
        ),
    )
