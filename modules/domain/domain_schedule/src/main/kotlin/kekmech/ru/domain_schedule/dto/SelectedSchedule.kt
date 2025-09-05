package kekmech.ru.domain_schedule.dto

import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.io.Serializable

public data class SelectedSchedule(
    val name: String,
    val type: ScheduleType,
) : Serializable
