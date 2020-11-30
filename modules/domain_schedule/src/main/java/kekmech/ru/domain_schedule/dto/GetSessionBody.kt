package kekmech.ru.domain_schedule.dto

data class GetSessionBody(
    val groupNumber: String
)

data class GetSessionResponse(
    val items: List<SessionItem>
)