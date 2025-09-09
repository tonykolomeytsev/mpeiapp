package kekmech.ru.feature_schedule_api.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
public data class SearchResult(
    val id: String,
    val name: String,
    val description: String,
    val type: ScheduleType,
) : Serializable

