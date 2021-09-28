package kekmech.ru.domain_schedule.dto

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalTime

@Parcelize
data class Schedule(
    val name: String = "",
    val id: String = "",
    val type: ScheduleType = ScheduleType.GROUP,
    val weeks: List<Week> = emptyList(),
) : Parcelable

@Parcelize
data class Week(
    val weekOfYear: Int = 0,
    val weekOfSemester: Int = 0,
    val firstDayOfWeek: LocalDate = LocalDate.now(),
    val days: List<Day> = emptyList(),
) : Parcelable

@Parcelize
data class Day(
    val dayOfWeek: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val classes: List<Classes> = emptyList(),
) : Parcelable

@Parcelize
data class Classes(
    val name: String = "",
    val type: ClassesType = ClassesType.UNDEFINED,
    val rawType: String? = null,
    val place: String = "",
    val groups: String = "",
    val person: String = "",
    val time: Time = Time(),
    val number: Int,
    // auxiliary fields
    var attachedNotePreview: String? = null,
    val progress: Float? = null,
) : Parcelable {
    // its to optimize recyclerview items
    // sets the same as parent schedule type
    @IgnoredOnParcel
    var scheduleType: ScheduleType = ScheduleType.GROUP
}

@Parcelize
data class Time(
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now(),
) : Parcelable

@Parcelize
enum class ClassesType : Parcelable {
    UNDEFINED,
    LECTURE,
    PRACTICE,
    LAB,
    COURSE,
    EXAM,
    CONSULTATION,
}

@Parcelize
enum class ScheduleType(val pathName: String) : Parcelable {
    GROUP("group"),
    PERSON("person"),
}
