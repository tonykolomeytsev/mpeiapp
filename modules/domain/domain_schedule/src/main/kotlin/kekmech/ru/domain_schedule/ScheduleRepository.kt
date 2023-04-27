package kekmech.ru.domain_schedule

import android.content.SharedPreferences
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kekmech.ru.common_cache.persistent_cache.PersistentCache
import kekmech.ru.common_kotlin.moscowLocalDate
import kekmech.ru.common_shared_preferences.string
import kekmech.ru.domain_schedule.dto.SearchResultType
import kekmech.ru.domain_schedule_models.dto.Schedule
import kekmech.ru.domain_schedule_models.dto.ScheduleType
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

private const val KEY_SELECTED_GROUP = "selected_group"
val GROUP_NUMBER_PATTERN = "[а-яА-Я]+-[а-яА-Я0-9]+-[0-9]+".toRegex()
val PERSON_NAME_PATTERN = "[а-яА-Я]+\\s+([а-яА-Я]+\\s?)+".toRegex()

@Suppress("TooManyFunctions")
class ScheduleRepository(
    private val scheduleService: ScheduleService,
    private val persistentCache: PersistentCache,
    sharedPreferences: SharedPreferences,
) {

    private var selectedScheduleName by sharedPreferences.string(KEY_SELECTED_GROUP)

    fun loadSchedule(
        scheduleName: String = selectedScheduleName,
        weekOffset: Int = 0,
    ): Single<Schedule> =
        scheduleService
            .getSchedule(getScheduleType(scheduleName).pathName, scheduleName, weekOffset)
            .orFromPersistentCache(scheduleName to weekOffset, persistentCache)

    fun selectSchedule(name: String): Completable =
        Completable.fromAction {
            selectedScheduleName = name
            FirebaseCrashlytics.getInstance().setCustomKey("schedule_name", name)
        }

    fun getSelectedScheduleName(): Single<String> = Single.just(selectedScheduleName)

    fun getSession(scheduleType: String = selectedScheduleName) = scheduleService
        .getSession(getScheduleType(scheduleType).pathName, scheduleType)

    fun getSearchResults(query: String, type: SearchResultType? = null) =
        scheduleService.getSearchResults(query, type)

    fun debugClearSelectedGroup() {
        selectedScheduleName = ""
    }

    private fun getScheduleType(scheduleName: String) = when {
        scheduleName.matches(GROUP_NUMBER_PATTERN) -> ScheduleType.GROUP
        else -> ScheduleType.PERSON
    }

    fun getSelectedScheduleNameForAnalytics() = selectedScheduleName
}

fun Single<Schedule>.orFromPersistentCache(
    key: Pair<String, Int>,
    persistentCache: PersistentCache,
): Single<Schedule> = this
    .doOnSuccess { schedule ->
        val (scheduleName, _) = key
        val weekOfYear = schedule.weeks.first().weekOfYear
        val cacheKey = "${scheduleName}_$weekOfYear"
        persistentCache.put(cacheKey, schedule)
    }
    .onErrorResumeNext {
        val (scheduleName, weekOffset) = key
        val weekOfYear = moscowLocalDate().weekOfYear() + weekOffset // на бекенде смещение, это ошибка
        val cacheKey = "${scheduleName}_$weekOfYear"
        persistentCache.getOrError(cacheKey, Schedule::class.java, null)
    }

private fun LocalDate.weekOfYear(): Int =
    get(WeekFields.of(Locale.ENGLISH).weekOfWeekBasedYear())
