package kekmech.ru.domain_schedule

import android.content.SharedPreferences
import io.reactivex.subjects.BehaviorSubject
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_schedule.dto.GetScheduleBody

private const val KEY_SELECTED_GROUP = "selected_group"

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val sharedPreferences: SharedPreferences
) {

    private var selectedGroup by sharedPreferences.string(KEY_SELECTED_GROUP)

    fun loadSchedule(groupName: String, weekOffset: Int = 0) = scheduleService
        .getSchedule(GetScheduleBody(groupName, weekOffset))

    val s = BehaviorSubject<String>.

    fun observeSchedule(weekOffset: Int = 0) = loadSchedule(selectedGroup, weekOffset)
}