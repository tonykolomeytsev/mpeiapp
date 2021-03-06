package kekmech.ru.domain_schedule.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    val name: String = "",
    val id: String = "",
    val type: ScheduleType = ScheduleType.GROUP,
    val weeks: List<Week> = emptyList()
) : Serializable

data class Week(
    val weekOfYear: Int = 0,
    val weekOfSemester: Int = 0,
    val firstDayOfWeek: LocalDate = LocalDate.now(),
    val days: List<Day> = emptyList()
) : Serializable

data class Day(
    val dayOfWeek: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val classes: List<Classes> = emptyList()
) : Serializable

data class Classes(
    val name: String = "",
    val type: ClassesType = ClassesType.UNDEFINED,
    val rawType: String? = null,
    val place: String = "",
    val groups: String = "",
    val person: String = "",
    val time: Time = Time(),
    val number: Int,
    @Transient var attachedNotePreview: String? = null,
    @Transient val progress: Float? = null
) : Serializable {
    // its to optimize recyclerview items
    // sets the same as parent schedule type
    @Transient var scheduleType: ScheduleType = ScheduleType.GROUP
}

data class Time(
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now()
) : Serializable

enum class ClassesType : Serializable { UNDEFINED, LECTURE, PRACTICE, LAB, COURSE, EXAM, CONSULTATION }

enum class ScheduleType(val pathName: String) : Serializable { GROUP("group"), PERSON("person") }
