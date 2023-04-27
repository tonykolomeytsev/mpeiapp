package kekmech.ru.domain_dashboard.dto

import java.time.LocalDate
import kotlin.time.Duration

sealed interface TimePrediction
{

    data class WithinOneDay(val duration: Duration) : TimePrediction

    data class WithinAWeek(
        val date: LocalDate,
        val duration: Duration,
    ) : TimePrediction
}
