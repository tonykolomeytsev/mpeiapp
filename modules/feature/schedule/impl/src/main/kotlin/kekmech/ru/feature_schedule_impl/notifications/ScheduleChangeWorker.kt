package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.time.LocalDate

/**
 * Background worker that periodically checks for schedule changes
 * and sends notifications to the user
 */
internal class ScheduleChangeWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val scheduleRepository: ScheduleRepository by inject()
    private val notificationManager: ScheduleNotificationManager by inject()
    private val cacheManager: ScheduleCacheManager by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Timber.d("ScheduleChangeWorker: Starting schedule check")

            val currentSchedule = try {
                scheduleRepository.getSelectedSchedule()
            } catch (e: Exception) {
                Timber.w(e, "ScheduleChangeWorker: No schedule selected")
                return@withContext Result.success()
            }

            val today = LocalDate.now()
            val futureChanges = mutableListOf<ScheduleChange>()
            var fetchedAny = false

            for (weekOffset in 0 until WEEKS_TO_CHECK) {
                val freshSchedule = scheduleRepository.getSchedule(
                    type = currentSchedule.type,
                    name = currentSchedule.name,
                    weekOffset = weekOffset,
                ).getOrNull()

                if (freshSchedule == null) {
                    Timber.w("ScheduleChangeWorker: Failed to fetch week offset $weekOffset")
                    continue
                }
                fetchedAny = true

                val cachedSchedule = cacheManager.getCachedSchedule(
                    name = currentSchedule.name,
                    type = currentSchedule.type,
                    weekOffset = weekOffset,
                )

                futureChanges += ScheduleChangeDetector
                    .detectChanges(oldSchedule = cachedSchedule, newSchedule = freshSchedule)
                    .filter { it.date >= today }

                cacheManager.cacheSchedule(
                    name = currentSchedule.name,
                    type = currentSchedule.type,
                    schedule = freshSchedule,
                    weekOffset = weekOffset,
                )
            }

            if (!fetchedAny) {
                return@withContext Result.retry()
            }

            if (futureChanges.isNotEmpty()) {
                notificationManager.showScheduleChangesNotification(futureChanges)
                Timber.i("ScheduleChangeWorker: Sent notification for ${futureChanges.size} changes")
            } else {
                Timber.d("ScheduleChangeWorker: No changes detected")
            }
            ScheduleNotificationPrefs.recordCheck(applicationContext, futureChanges.size)

            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "ScheduleChangeWorker: Unexpected error")
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "schedule_change_check"
        const val IMMEDIATE_WORK_NAME = "schedule_change_check_now"

        /**
         * Unique work tag for this worker
         */
        const val WORK_TAG = "schedule_change_worker"

        /** Current week plus the next two */
        private const val WEEKS_TO_CHECK = 3
    }
}
