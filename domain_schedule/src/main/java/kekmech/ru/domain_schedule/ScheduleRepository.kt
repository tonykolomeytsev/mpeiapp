package kekmech.ru.domain_schedule

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import kekmech.ru.common_persistent_cache.orFromCache
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_schedule.dto.GetScheduleBody

private const val KEY_SELECTED_GROUP = "selected_group"

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val schedulePersistentCache: SchedulePersistentCache,
    sharedPreferences: SharedPreferences
) {

    private var selectedGroup by sharedPreferences.string(KEY_SELECTED_GROUP)

    fun loadSchedule(groupName: String = selectedGroup, weekOffset: Int = 0) = scheduleService
        .getSchedule(GetScheduleBody(groupName, weekOffset))
        .orFromCache(groupName to weekOffset, schedulePersistentCache)

    fun selectGroup(groupName: String): Completable = Completable.fromAction {
        selectedGroup = groupName
    }

    fun getSelectedGroup() = Single.just(selectedGroup)

    fun debugClearSelectedGroup() {
        if (BuildConfig.DEBUG) {
            selectedGroup = ""
        } else {
            error("Method call is allowed only in debug mode!")
        }
    }
}