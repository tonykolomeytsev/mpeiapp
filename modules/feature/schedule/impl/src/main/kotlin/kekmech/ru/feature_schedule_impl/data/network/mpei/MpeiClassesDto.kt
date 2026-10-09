package kekmech.ru.feature_schedule_impl.data.network.mpei

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MpeiClassesDto(
    @SerialName("auditorium")
    val auditorium: String? = null,
    @SerialName("beginLesson")
    val beginLesson: String,
    @SerialName("endLesson")
    val endLesson: String,
    @SerialName("date")
    val date: String,
    @SerialName("discipline")
    val discipline: String,
    @SerialName("kindOfWork")
    val kindOfWork: String? = null,
    @SerialName("lecturer")
    val lecturer: String? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("subGroup")
    val subGroup: String? = null,
    @SerialName("stream")
    val stream: String? = null,
    @SerialName("dayOfWeek")
    val dayOfWeek: Int? = null,
    @SerialName("lessonNumberStart")
    val lessonNumberStart: Int? = null,
)
