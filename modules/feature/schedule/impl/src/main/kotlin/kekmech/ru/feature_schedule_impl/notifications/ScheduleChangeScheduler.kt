package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Manages scheduling of schedule change checks
 */
class ScheduleChangeScheduler(
    private val context: Context
) {
    
    private val workManager = WorkManager.getInstance(context)
    
    /**
     * Start periodic schedule change checks
     * 
     * @param intervalHours Interval between checks in hours (default: 6 hours)
     */
    fun startPeriodicChecks(intervalHours: Long = DEFAULT_CHECK_INTERVAL_HOURS) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Requires internet
            .setRequiresBatteryNotLow(true) // Only when battery is not low
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<ScheduleChangeWorker>(
            repeatInterval = intervalHours,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .addTag(ScheduleChangeWorker.WORK_TAG)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            ScheduleChangeWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, // Apply new interval if already scheduled
            workRequest
        )
        
        Timber.i("ScheduleChangeScheduler: Periodic checks started (every $intervalHours hours)")
    }
    
    /**
     * Stop periodic schedule change checks
     */
    fun stopPeriodicChecks() {
        workManager.cancelUniqueWork(ScheduleChangeWorker.WORK_NAME)
        Timber.i("ScheduleChangeScheduler: Periodic checks stopped")
    }
    
    /**
     * Check if periodic checks are currently enabled
     */
    suspend fun arePeriodicChecksEnabled(): Boolean {
        val workInfos = workManager
            .getWorkInfosForUniqueWorkFlow(ScheduleChangeWorker.WORK_NAME)
            .first()
        return workInfos.any { !it.state.isFinished }
    }
    
    /**
     * Trigger immediate schedule check (one-time)
     */
    fun triggerImmediateCheck() {
        val workRequest = OneTimeWorkRequestBuilder<ScheduleChangeWorker>()
            .addTag(ScheduleChangeWorker.WORK_TAG)
            .build()
        
        workManager.enqueue(workRequest)
        
        Timber.i("ScheduleChangeScheduler: Immediate check triggered")
    }
    
    companion object {
        /**
         * Default interval for periodic checks (6 hours)
         */
        const val DEFAULT_CHECK_INTERVAL_HOURS = 6L
        
        /**
         * Minimum check interval (1 hour)
         */
        const val MIN_CHECK_INTERVAL_HOURS = 1L
        
        /**
         * Maximum check interval (24 hours)
         */
        const val MAX_CHECK_INTERVAL_HOURS = 24L
    }
}
