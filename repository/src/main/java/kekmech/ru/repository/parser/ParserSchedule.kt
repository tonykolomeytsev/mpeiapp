package kekmech.ru.repository.parser

import java.util.*

data class ParserSchedule(
    val couples: List<ParserCouple>,
    val firstCoupleDay: Calendar
)