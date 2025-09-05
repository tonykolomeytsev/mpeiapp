package kekmech.ru.domain_dashboard.dto

import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kekmech.ru.domain_schedule_models.dto.WeekOfSemester

public data class ScheduleMetaInfo(
    val name: String,
    val type: ScheduleType,
    val weekOfSemester: WeekOfSemester,
)
