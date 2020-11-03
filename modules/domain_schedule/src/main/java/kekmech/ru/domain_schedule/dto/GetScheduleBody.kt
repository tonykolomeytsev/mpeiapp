package kekmech.ru.domain_schedule.dto

data class GetScheduleBody(
    val groupNumber: String,
    val weekOffset: Int = 0
)