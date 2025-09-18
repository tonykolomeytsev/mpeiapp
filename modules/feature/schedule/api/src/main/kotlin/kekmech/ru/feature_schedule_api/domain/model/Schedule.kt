package kekmech.ru.feature_schedule_api.domain.model

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

public data class Schedule(
    val name: String,
    val id: String,
    val type: ScheduleType,
    val weeks: List<Week>,
) : Serializable

public data class Week(
    val weekOfYear: Int,
    val weekOfSemester: WeekOfSemester,
    val firstDayOfWeek: LocalDate,
    val days: List<Day>,
) : Serializable

public sealed interface WeekOfSemester : Serializable {

    public data object NonStudying : WeekOfSemester {
        @Suppress("unused")
        private fun readResolve(): Any = NonStudying
    }

    public data class Studying(val num: Int) : WeekOfSemester
}

public data class Day(
    val dayOfWeek: Int,
    val date: LocalDate,
    val classes: List<Classes>,
) : Serializable

public data class Classes(
    val name: String,
    val type: ClassesType,
    val rawType: String?,
    val place: String,
    val groups: String,
    val person: String,
    val time: Time,
    val number: Int,

    // for ui purposes
    var attachedNotePreview: String? = null,
    val progress: Float? = null,
) : Serializable {

    // its to optimize recyclerview items
    // sets the same as parent schedule type
    var scheduleType: ScheduleType = ScheduleType.GROUP
}

public data class Time(
    val start: LocalTime,
    val end: LocalTime,
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
