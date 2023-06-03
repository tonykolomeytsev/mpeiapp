package kekmech.ru.domain_schedule.data

import android.content.SharedPreferences
import kekmech.ru.domain_schedule.dto.SelectedSchedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Wrapper to support migration of old versions of the app to the new data format
 *
 * In the early stages of its existence, the app could only display group schedules.
 * Therefore, the name of the selected group was stored in [SharedPreferences] in the
 * `selected_group` text field.
 *
 * Currently, this field can hold the name of any schedule, including teacher and classroom
 * names. In connection with the emergence of new types of schedules, it became necessary
 * to store the type of schedules in a separate field. Thus, the complete key of a
 * schedule is not only its name, but also its type.
 */
internal class SelectedScheduleSource(
    private val sharedPreferences: SharedPreferences,
) : ReadWriteProperty<Any, SelectedSchedule> {


    private var selectedSchedule: SelectedSchedule? = null

    override fun getValue(
        thisRef: Any,
        property: KProperty<*>,
    ): SelectedSchedule {
        selectedSchedule?.let { return it }
        // load name from preferences and interrupt process if there is no name. Empty schedule
        // name is initial app state which let us understand we should start onboarding
        val selectedScheduleName = sharedPreferences
            .getString(KeySelectedScheduleName, null)
            ?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("SelectedSchedule is empty")
        // derive type from name if it is absent in preferences (for app update v1+ to v2+)
        val rawType = sharedPreferences.getString(KeySelectedScheduleType, null).orEmpty()
        val selectedScheduleType = runCatching { ScheduleType.valueOf(rawType) }
            .getOrElse {
                when {
                    rawType.matches(GroupNumberPattern) -> ScheduleType.GROUP
                    else -> ScheduleType.PERSON
                }
            }
        return SelectedSchedule(
            name = selectedScheduleName,
            type = selectedScheduleType,
        ).also { selectedSchedule = it }
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: SelectedSchedule,
    ) {
        selectedSchedule = value
        sharedPreferences
            .edit()
            .putString(KeySelectedScheduleName, value.name)
            .putString(KeySelectedScheduleType, value.type.toString())
            .apply()
    }

    companion object {

        private const val KeySelectedScheduleName = "selected_group"
        private const val KeySelectedScheduleType = "selected_schedule_type"

        val GroupNumberPattern = "[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()
    }
}
