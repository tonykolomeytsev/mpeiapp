package kekmech.ru.feature_favorite_schedule_api.domain.model

import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.io.Serializable

data class FavoriteSchedule(
    val name: String,
    val type: ScheduleType,
    val description: String,
    val order: Int,
) : Serializable
