package kekmech.ru.domain_schedule

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import kekmech.ru.common_persistent_cache.orFromPersistentCache
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.GetScheduleBody
import kekmech.ru.domain_schedule.sources.FavoriteSource

private const val KEY_SELECTED_GROUP = "selected_group"

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val schedulePersistentCache: SchedulePersistentCache,
    sharedPreferences: SharedPreferences,
    private val favoriteSource: FavoriteSource
) {

    private var selectedGroup by sharedPreferences.string(KEY_SELECTED_GROUP)

    fun loadSchedule(groupName: String = selectedGroup, weekOffset: Int = 0) = scheduleService
        .getSchedule(GetScheduleBody(groupName, weekOffset))
        .orFromPersistentCache(groupName to weekOffset, schedulePersistentCache)

    fun selectGroup(groupName: String): Completable = Completable.fromAction {
        selectedGroup = groupName
    }

    fun getSelectedGroup() = Single.just(selectedGroup)

    fun getFavorites(): Single<List<FavoriteSchedule>> =
        Single.just(favoriteSource.getAll())

    fun setFavorites(favorites: List<FavoriteSchedule>): Completable = Completable.fromRunnable {
        favoriteSource.deleteAll()
        favoriteSource.addAll(favorites)
    }

    fun debugClearSelectedGroup() {
        if (BuildConfig.DEBUG) {
            selectedGroup = ""
        } else {
            error("Method call is allowed only in debug mode!")
        }
    }
}