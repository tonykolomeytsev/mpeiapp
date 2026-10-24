package kekmech.ru.feature_schedule_impl.notifications.reminders

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_impl.notifications.reminders.ClassReminderPrefs.activeKinds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId

/**
 * Reads the selected schedule (the repository falls back to the cache when offline) and
 * re-registers the class reminder alarms. Runs periodically, on app start, after a schedule
 * change, a reboot and a time zone / clock change.
 */
internal class ClassReminderSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val scheduleRepository: ScheduleRepository by inject()
    private val alarmScheduler: ClassReminderAlarmScheduler by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val kinds = ClassReminderPrefs.of(applicationContext).activeKinds()
            if (kinds.isEmpty()) {
                alarmScheduler.cancelAll()
                return@withContext Result.success()
            }
            val selected = runCatching { scheduleRepository.getSelectedSchedule() }.getOrNull()
                ?: return@withContext Result.success() // no schedule selected yet

            val schedules = (0 until WEEKS_TO_PLAN).mapNotNull { weekOffset ->
                scheduleRepository.getSchedule(selected.type, selected.name, weekOffset).getOrNull()
            }
            if (schedules.isEmpty()) {
                // keep the alarms registered earlier, try again with backoff
                return@withContext Result.retry()
            }
            val reminders = ClassReminderPlanner.plan(
                schedules = schedules,
                now = Instant.now(),
                zone = ZoneId.systemDefault(),
                kinds = kinds,
            )
            alarmScheduler.apply(reminders)
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "ClassReminderSyncWorker: unexpected error")
            Result.failure()
        }
    }

    companion object {
        const val PERIODIC_WORK_NAME = "class_reminders_sync"
        const val IMMEDIATE_WORK_NAME = "class_reminders_sync_now"

        /** Current week plus the next two */
        private const val WEEKS_TO_PLAN = 3
    }
}
