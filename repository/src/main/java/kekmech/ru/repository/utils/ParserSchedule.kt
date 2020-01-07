package kekmech.ru.repository.utils

import java.util.*

data class ParserSchedule(
    val couples: List<ParserCouple>,
    val firstCoupleDay: Calendar
)