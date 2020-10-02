package kekmech.ru.domain_schedule.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class Schedule(
    val groupNumber: String = "",
    val groupId: String = "",
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
    val place: String = "",
    val groups: String = "",
    val person: String = "",
    val time: Time = Time(),
    val number: Int
) : Serializable {
    @Transient var stackType: ClassesStackType? = null
    @Transient var hasAttachedNote: Boolean = false
}

data class Time(
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now()
) : Serializable

enum class ClassesType : Serializable { UNDEFINED, LECTURE, PRACTICE, LAB, COURSE }

enum class ClassesStackType { START, MIDDLE, END }
