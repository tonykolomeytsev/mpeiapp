package kekmech.ru.addscreen.parser

import java.util.*

data class ParserSchedule(
    val couples: List<ParserCouple>,
    val firstCoupleDay: Calendar
)