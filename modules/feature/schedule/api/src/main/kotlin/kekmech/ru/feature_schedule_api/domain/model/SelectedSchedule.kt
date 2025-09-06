package kekmech.ru.feature_schedule_api.domain.model

import java.io.Serializable

public data class SelectedSchedule(
    val name: String,
    val type: ScheduleType,
) : Serializable
