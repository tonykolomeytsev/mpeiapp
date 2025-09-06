package kekmech.ru.feature_favorite_schedule_api.domain.model

import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import java.io.Serializable

public data class FavoriteSchedule(
    val name: String,
    val type: ScheduleType,
    val description: String,
    val order: Int,
) : Serializable
