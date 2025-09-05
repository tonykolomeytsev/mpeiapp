package kekmech.ru.domain_schedule_models.dto

import com.google.gson.annotations.JsonAdapter
import kekmech.ru.domain_schedule_models.WeekOfSemesterJsonAdapter
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

public data class Schedule(
    val name: String = "",
    val id: String = "",
    val type: ScheduleType = ScheduleType.GROUP,
    val weeks: List<Week> = emptyList(),
) : Serializable

public data class Week(
    val weekOfYear: Int = 0,
    val weekOfSemester: WeekOfSemester = WeekOfSemester.NonStudying,
    val firstDayOfWeek: LocalDate = LocalDate.now(),
    val days: List<Day> = emptyList(),
) : Serializable

@JsonAdapter(value = WeekOfSemesterJsonAdapter::class, nullSafe = true)
public sealed interface WeekOfSemester {

    public object NonStudying : WeekOfSemester
    public data class Studying(val num: Int) : WeekOfSemester
}

public data class Day(
    val dayOfWeek: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val classes: List<Classes> = emptyList(),
) : Serializable

public data class Classes(
    val name: String = "",
    val type: ClassesType = ClassesType.UNDEFINED,
    val rawType: String? = null,
    val place: String = "",
    val groups: String = "",
    val person: String = "",
    val time: Time = Time(),
    val number: Int,
    @Transient var attachedNotePreview: String? = null,
    @Transient val progress: Float? = null,
) : Serializable {

    // its to optimize recyclerview items
    // sets the same as parent schedule type
    @Transient
    var scheduleType: ScheduleType = ScheduleType.GROUP
}

public data class Time(
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now(),
) : Serializable

public enum class ClassesType : Serializable {
    UNDEFINED,
    LECTURE,
    PRACTICE,
    LAB,
    COURSE,
    EXAM,
    CONSULTATION,
}

public enum class ScheduleType(public val pathName: String) : Serializable {
    GROUP("group"),
    PERSON("person"),
}
