package kekmech.ru.domain_schedule.dto

import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.io.Serializable

public data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: ScheduleType,
) : Serializable

