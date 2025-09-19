package kekmech.ru.mock_server.routing.schedule.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.serialization.Serializable

@Serializable
internal data class ScheduleDto(
    @SerialName("name")
    val name: String,

    @SerialName("id")
    val id: String,

    @SerialName("type")
    val type: ScheduleTypeDto,

    @SerialName("weeks")
    val weeks: List<WeekDto>,
)

@Serializable
internal data class WeekDto(
    @SerialName("weekOfYear")
    val weekOfYear: Int,

    @SerialName("weekOfSemester")
    val weekOfSemester: Int,

    @Contextual
    @SerialName("firstDayOfWeek")
    val firstDayOfWeek: LocalDate,

    @SerialName("days")
    val days: List<DayDto>,
)

@Serializable
internal data class DayDto(
    @SerialName("dayOfWeek")
    val dayOfWeek: Int,

    @Contextual
    @SerialName("date")
    val date: LocalDate = LocalDate.now(),

    @SerialName("classes")
    val classes: List<ClassesDto> = emptyList(),
)

@Serializable
internal data class ClassesDto(
    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: ClassesTypeDto,

    @SerialName("rawType")
    val rawType: String?,

    @SerialName("place")
    val place: String,

    @SerialName("groups")
    val groups: String,

    @SerialName("person")
    val person: String,

    @SerialName("time")
    val time: TimeDto,

    @SerialName("number")
    val number: Int,
)

@Serializable
internal data class TimeDto(
    @Contextual
    @SerialName("start")
    val start: LocalTime = LocalTime.now(),

    @Contextual
    @SerialName("end")
    val end: LocalTime = LocalTime.now(),
)

@Serializable
internal enum class ClassesTypeDto {
    @SerialName("LECTURE")
    LECTURE,

    @SerialName("PRACTICE")
    PRACTICE,

    @SerialName("LAB")
    LAB,

    @SerialName("COURSE")
    COURSE,

    @SerialName("EXAM")
    EXAM,

    @SerialName("CONSULTATION")
    CONSULTATION,

    @SerialName("UNDEFINED")
    UNDEFINED,
}

@Serializable
internal enum class ScheduleTypeDto {
    @SerialName("GROUP")
    GROUP,

    @SerialName("PERSON")
    PERSON,
}
