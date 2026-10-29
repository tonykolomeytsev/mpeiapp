package kekmech.ru.feature_schedule_impl.notifications.lockscreen

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_impl.domain.ClassTimeline
import kekmech.ru.feature_schedule_impl.notifications.lockscreen.LockScreenPrefs.isLockScreenEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Draws the lock screen notification for the current moment and sets the alarm for the next change.
 * The schedule comes from the repository (it falls back to the cache when offline).
 */
internal class LockScreenRefreshWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val scheduleRepository: ScheduleRepository by inject()
    private val notifier: LockScreenNotifier by inject()
    private val controller: LockScreenScheduleController by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            if (!LockScreenPrefs.of(applicationContext).isLockScreenEnabled()) {
                notifier.hide()
                return@withContext Result.success()
            }
            val selected = runCatching { scheduleRepository.getSelectedSchedule() }.getOrNull()
                ?: return@withContext Result.success() // no schedule selected yet

            val today = LocalDate.now()
            val schedule = scheduleRepository.getSchedule(selected.type, selected.name, weekOffset = 0).getOrNull()
                ?: return@withContext Result.retry()
            val classes = schedule.weeks.flatMap { it.days }.firstOrNull { it.date == today }?.classes.orEmpty()

            val now = LocalDateTime.now()
            val timeline = ClassTimeline.of(classes, now.toLocalTime())
            notifier.show(timeline)

            val zone = ZoneId.systemDefault()
            val nextRefresh = timeline.changesAt?.let { today.atTime(it) }
                // nothing changes today: look again right after midnight, the next day has other classes
                ?: today.plusDays(1).atTime(0, MIDNIGHT_SHIFT_MINUTES)
            controller.scheduleRefreshAt(nextRefresh.atZone(zone).toInstant().toEpochMilli())
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "LockScreenRefreshWorker: unexpected error")
            Result.failure()
        }
    }

    companion object {
        const val PERIODIC_WORK_NAME = "lock_screen_schedule_periodic"
        const val IMMEDIATE_WORK_NAME = "lock_screen_schedule_now"
        private const val MIDNIGHT_SHIFT_MINUTES = 5
    }
}
