package kekmech.ru.mock_server.routing.schedule

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.domain_schedule.dto.GetSearchResultsResponse
import kekmech.ru.domain_schedule.dto.SearchResult
import kekmech.ru.domain_schedule.dto.SearchResultType
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.mock_server.randomResponseDelay

internal fun Routing.getSearchResultsV1() {
    get("/schedule/v1/search") {
        randomResponseDelay()
        val type = call.request.queryParameters["type"]
        call.respond(createSearchResults(type?.let(ScheduleType::valueOf)))
    }
}

private fun createSearchResults(type: ScheduleType?): GetSearchResultsResponse =
    GetSearchResultsResponse(
        items = listOfNotNull(
            SearchResult(
                id = "12345",
                name = "Сэ-12-21",
                description = "Институт энергомашиностроения и механики | Очная",
                type = SearchResultType.GROUP,
            ).takeIf { type == ScheduleType.GROUP },
            SearchResult(
                id = "123456",
                name = "С-12-16",
                description = "Давно выпустившаяся группа",
                type = SearchResultType.GROUP,
            ).takeIf { type == ScheduleType.GROUP },
            SearchResult(
                id = "1234567",
                name = "Бободжанов Абдухафиз Абдурасулович",
                description = "Самый добрый преподаватель по вышмату",
                type = SearchResultType.PERSON,
            ).takeIf { type == ScheduleType.PERSON },
        ),
    )
