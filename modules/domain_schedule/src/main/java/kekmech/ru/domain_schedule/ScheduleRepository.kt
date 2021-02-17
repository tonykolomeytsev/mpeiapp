package kekmech.ru.domain_schedule

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Single
import kekmech.ru.common_persistent_cache.orFromPersistentCache
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_schedule.dto.FavoriteSchedule
import kekmech.ru.domain_schedule.dto.ScheduleType
import kekmech.ru.domain_schedule.dto.SearchResultType
import kekmech.ru.domain_schedule.sources.FavoriteSource

private const val KEY_SELECTED_GROUP = "selected_group"
val GROUP_NUMBER_PATTERN = "[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()
val PERSON_NAME_PATTERN = "[а-яА-Я]+\\s+([а-яА-Я]+\\s?)+".toRegex()

class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val schedulePersistentCache: SchedulePersistentCache,
    sharedPreferences: SharedPreferences,
    private val favoriteSource: FavoriteSource
) {

    private var selectedScheduleName by sharedPreferences.string(KEY_SELECTED_GROUP)

    fun loadSchedule(scheduleName: String = selectedScheduleName, weekOffset: Int = 0) = scheduleService
        .getSchedule(getScheduleType(scheduleName).pathName, scheduleName, weekOffset)
        .orFromPersistentCache(scheduleName to weekOffset, schedulePersistentCache)

    fun selectSchedule(name: String): Completable = Completable.fromAction {
        selectedScheduleName = name
    }

    fun getSelectedScheduleName() = Single.just(selectedScheduleName)

    fun getFavorites(): Single<List<FavoriteSchedule>> =
        Single.just(favoriteSource.getAll())

    fun setFavorites(favorites: List<FavoriteSchedule>): Completable = Completable.fromRunnable {
        favoriteSource.deleteAll()
        favoriteSource.addAll(favorites)
    }

    fun addFavorite(favoriteSchedule: FavoriteSchedule): Completable = Completable.fromRunnable {
        favoriteSource.add(favoriteSchedule)
    }

    fun removeFavorite(favoriteSchedule: FavoriteSchedule): Completable = Completable.fromRunnable {
        favoriteSource.remove(favoriteSchedule)
    }

    fun getSession(scheduleType: String = selectedScheduleName) = scheduleService
        .getSession(getScheduleType(scheduleType).pathName, scheduleType)

    fun getSearchResults(query: String, type: SearchResultType? = null) =
        scheduleService.getSearchResults(query, type)

    fun debugClearSelectedGroup() {
        if (BuildConfig.DEBUG) {
            selectedScheduleName = ""
        } else {
            error("Method call is allowed only in debug mode!")
        }
    }

    private fun getScheduleType(scheduleName: String) = when {
        scheduleName.matches(GROUP_NUMBER_PATTERN) -> ScheduleType.GROUP
        else -> ScheduleType.PERSON
    }
}