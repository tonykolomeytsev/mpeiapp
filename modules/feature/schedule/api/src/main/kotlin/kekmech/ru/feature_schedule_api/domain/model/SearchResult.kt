package kekmech.ru.feature_schedule_api.domain.model

import java.io.Serializable

data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: ScheduleType,
) : Serializable

