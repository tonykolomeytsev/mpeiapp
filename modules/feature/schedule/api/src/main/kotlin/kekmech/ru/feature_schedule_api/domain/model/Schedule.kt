package kekmech.ru.feature_schedule_api.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.JsonAdapter
import kekmech.ru.feature_schedule_api.data.network.WeekOfSemesterJsonAdapter
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Keep
public data class Schedule(
    val name: String = "",
    val id: String = "",
    val type: ScheduleType = ScheduleType.GROUP,
    val weeks: List<Week> = emptyList(),
) : Serializable

@Keep
public data class Week(
    val weekOfYear: Int = 0,
    val weekOfSemester: WeekOfSemester = WeekOfSemester.NonStudying,
    val firstDayOfWeek: LocalDate = LocalDate.now(),
    val days: List<Day> = emptyList(),
) : Serializable

@Keep
@JsonAdapter(value = WeekOfSemesterJsonAdapter::class, nullSafe = true)
public sealed interface WeekOfSemester : Serializable {

    public data object NonStudying : WeekOfSemester {
        private fun readResolve(): Any = NonStudying
    }

    public data class Studying(val num: Int) : WeekOfSemester
}

@Keep
public data class Day(
    val dayOfWeek: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val classes: List<Classes> = emptyList(),
) : Serializable

@Keep
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

@Keep
public data class Time(
    val start: LocalTime = LocalTime.now(),
    val end: LocalTime = LocalTime.now(),
) : Serializable

@Keep
public enum class ClassesType : Serializable {
    UNDEFINED,
    LECTURE,
    PRACTICE,
    LAB,
    COURSE,
    EXAM,
    CONSULTATION,
}

@Keep
public enum class ScheduleType(public val pathName: String) : Serializable {
    GROUP("group"),
    PERSON("person"),
}
