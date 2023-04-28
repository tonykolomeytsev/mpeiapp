package kekmech.ru.domain_favorite_schedule.dto

import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.io.Serializable

data class FavoriteSchedule(
    val name: String,
    val type: ScheduleType,
    val description: String,
    val order: Int,
) : Serializable
