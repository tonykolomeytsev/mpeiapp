package kekmech.ru.domain_schedule.dto

import java.time.LocalDate

data class SessionItem(
    val name: String = "",
    val type: SessionItemType = SessionItemType.UNDEFINED,
    val place: String = "",
    val person: String = "",
    val groups: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: Time = Time()
)

enum class SessionItemType { UNDEFINED, CONSULTATION, EXAM }
