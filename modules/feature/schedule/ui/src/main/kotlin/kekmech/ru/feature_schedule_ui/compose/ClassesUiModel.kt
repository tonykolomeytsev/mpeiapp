package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.runtime.Immutable
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.feature_schedule_ui.utils.secondaryLabel
import java.time.LocalTime

/**
 * UI model of the [ClassesItem]. Contains only the data the card renders,
 * so that the domain [Classes] doesn't leak into the composables.
 *
 * Use [toUiModel] to create it from the domain [Classes].
 *
 * @param name name of the discipline
 * @param type type of the classes, defines the type chip text and colors
 * @param rawType type name from the backend, shown for [ClassesType.UNDEFINED]
 * @param number number of the classes in the day, starting from 1
 * @param place auditorium of the classes
 * @param secondaryLabel person and subgroup for group schedules, or groups for person schedules
 * @param startTime start time of the classes
 * @param endTime end time of the classes
 * @param notePreview text of the note attached to the classes, or null if there is no note
 * @param progress progress of the ongoing classes from 0 to 1, or null if classes are not ongoing
 */
@Immutable
public data class ClassesUiModel(
    val name: String,
    val type: ClassesType,
    val rawType: String?,
    val number: Int,
    val place: String,
    val secondaryLabel: String?,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val notePreview: String? = null,
    val progress: Float? = null,
)

/**
 * Maps the domain [Classes] to the [ClassesUiModel].
 *
 * @param scheduleType type of the schedule the classes belong to
 * @param notePreview text of the note attached to the classes, or null if there is no note
 * @param progress progress of the ongoing classes from 0 to 1, or null if classes are not ongoing
 */
public fun Classes.toUiModel(
    scheduleType: ScheduleType,
    // TODO: Pass the note explicitly, when the AttachNotesToScheduleService stops mutating Classes
    notePreview: String? = attachedNotePreview,
    progress: Float? = this.progress,
): ClassesUiModel =
    ClassesUiModel(
        name = name,
        type = type,
        rawType = rawType,
        number = number,
        place = place,
        secondaryLabel = secondaryLabel(scheduleType),
        startTime = time.start,
        endTime = time.end,
        notePreview = notePreview,
        progress = progress,
    )
