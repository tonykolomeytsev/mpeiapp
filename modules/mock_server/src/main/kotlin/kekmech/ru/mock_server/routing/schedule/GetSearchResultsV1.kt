package kekmech.ru.mock_server.routing.schedule

import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kekmech.ru.mock_server.randomResponseDelay
import kekmech.ru.mock_server.routing.schedule.model.ScheduleTypeDto
import kekmech.ru.mock_server.routing.schedule.model.SearchResultDto

internal fun Routing.getSearchResultsV1() {
    get("/schedule/v1/search") {
        randomResponseDelay()
        val type = call.request.queryParameters["type"]
        call.respond(createSearchResults(type?.let(ScheduleTypeDto::valueOf)))
    }
}

@Suppress("unused")
private class GetSearchResultsResponse(
    val items: List<SearchResultDto>,
)

private fun createSearchResults(type: ScheduleTypeDto?): GetSearchResultsResponse =
    GetSearchResultsResponse(
        items = listOfNotNull(
            SearchResultDto(
                id = "12345",
                name = "Сэ-12-21",
                description = "Институт энергомашиностроения и механики | Очная",
                type = ScheduleTypeDto.GROUP,
            ).takeIf { type == ScheduleTypeDto.GROUP },
            SearchResultDto(
                id = "123456",
                name = "С-12-16",
                description = "Давно выпустившаяся группа",
                type = ScheduleTypeDto.GROUP,
            ).takeIf { type == ScheduleTypeDto.GROUP },
            SearchResultDto(
                id = "1234567",
                name = "Бободжанов Абдухафиз Абдурасулович",
                description = "Самый добрый преподаватель по вышмату",
                type = ScheduleTypeDto.PERSON,
            ).takeIf { type == ScheduleTypeDto.PERSON },
        ),
    )
