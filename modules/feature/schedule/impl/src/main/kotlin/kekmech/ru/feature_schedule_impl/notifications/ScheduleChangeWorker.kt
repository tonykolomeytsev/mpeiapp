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
            
            // Get currently selected schedule
            val currentSchedule = try {
                scheduleRepository.getSelectedSchedule()
            } catch (e: Exception) {
                Timber.w(e, "ScheduleChangeWorker: No schedule selected")
                return@withContext Result.success()
            }

            // Fetch fresh schedule from API
            val freshSchedule = try {
                scheduleRepository.getSchedule(
                    type = currentSchedule.type,
                    name = currentSchedule.name,
                    weekOffset = 0,
                ).getOrThrow()
            } catch (e: Exception) {
                Timber.e(e, "ScheduleChangeWorker: Failed to fetch schedule")
                return@withContext Result.retry()
            }
            
            // Get cached schedule for comparison
            val cachedSchedule = cacheManager.getCachedSchedule(
                name = currentSchedule.name,
                type = currentSchedule.type
            )
            
            // Detect changes
            val changes = ScheduleChangeDetector.detectChanges(
                oldSchedule = cachedSchedule,
                newSchedule = freshSchedule
            )
            
            if (changes.isNotEmpty()) {
                Timber.d("ScheduleChangeWorker: Detected ${changes.size} changes")
                
                // Filter only future changes
                val futureChanges = changes.filter { change ->
                    change.date >= LocalDate.now()
                }
                
                if (futureChanges.isNotEmpty()) {
                    // Send notification
                    notificationManager.showScheduleChangesNotification(futureChanges)
                    
                    Timber.i("ScheduleChangeWorker: Sent notification for ${futureChanges.size} changes")
                }
            } else {
                Timber.d("ScheduleChangeWorker: No changes detected")
            }
            
            // Update cache with fresh schedule
            cacheManager.cacheSchedule(
                name = currentSchedule.name,
                type = currentSchedule.type,
                schedule = freshSchedule
            )
            
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "ScheduleChangeWorker: Unexpected error")
            Result.failure()
        }
    }

    companion object {
        const val WORK_NAME = "schedule_change_check"
        
        /**
         * Unique work tag for this worker
         */
        const val WORK_TAG = "schedule_change_worker"
    }
}
