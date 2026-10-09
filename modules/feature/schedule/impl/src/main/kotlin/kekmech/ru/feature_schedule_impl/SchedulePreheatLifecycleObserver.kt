package kekmech.ru.feature_schedule_impl

import android.content.Context
import androidx.core.content.edit
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.ScheduleType
import kekmech.ru.lib_app_database.dao.FavoriteScheduleDao
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Preloads the schedule of the selected group/person and all favorite schedules
 * for [PREHEAT_WEEKS_AHEAD] weeks ahead into the persistent cache on app start.
 *
 * Two months of preload guarantee that any recurrent lesson appears in the cache
 * at least twice, so the schedule stays available without a network connection.
 *
 * Successful responses are stored by `ScheduleRepositoryImpl` itself
 * (see `ScheduleCacheWrapper`), failed requests are ignored: the next app start
 * on another day will retry the preload.
 */
internal class SchedulePreheatLifecycleObserver(
    private val scheduleRepository: ScheduleRepository,
    private val favoriteScheduleDao: FavoriteScheduleDao,
) : AppLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        if (prefs.getString(KEY_LAST_PREHEAT_DATE, null) == today) return

        scope.launch {
            val anySucceeded = preheat()
            if (anySucceeded) {
                prefs.edit { putString(KEY_LAST_PREHEAT_DATE, today) }
            }
        }
    }

    /**
     * @return true if at least one weekly schedule was successfully fetched
     */
    private suspend fun preheat(): Boolean {
        val targets = LinkedHashMap<String, ScheduleType>()

        runCatching { scheduleRepository.getSelectedSchedule() }
            .getOrNull()
            ?.let { targets[it.name] = it.type }

        runCatching { favoriteScheduleDao.getAll() }
            .getOrDefault(emptyList())
            .forEach { entity ->
                ScheduleType.entries
                    .firstOrNull { it.name.equals(entity.type, ignoreCase = true) }
                    ?.let { type -> targets.putIfAbsent(entity.name, type) }
            }

        var anySucceeded = false
        targets.forEach { (name, type) ->
            for (weekOffset in 0..PREHEAT_WEEKS_AHEAD) {
                scheduleRepository.getSchedule(type, name, weekOffset)
                    .onSuccess { anySucceeded = true }
            }
        }
        return anySucceeded
    }

    private companion object {
        const val PREFS_NAME = "schedule_preheat"
        const val KEY_LAST_PREHEAT_DATE = "last_preheat_date"

        /** Weeks ahead to preload: 9 weeks cover two months */
        const val PREHEAT_WEEKS_AHEAD = 8
    }
}
