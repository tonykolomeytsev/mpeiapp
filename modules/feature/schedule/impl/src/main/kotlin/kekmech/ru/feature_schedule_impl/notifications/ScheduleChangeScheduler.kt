package kekmech.ru.feature_schedule_impl.notifications

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Manages scheduling of schedule change checks
 */
internal class ScheduleChangeScheduler(
    private val context: Context
) {

    private val workManager get() = WorkManager.getInstance(context)

    /**
     * Start periodic schedule change checks
     *
     * @param intervalHours Interval between checks in hours (default: 6 hours)
     * @param replaceExisting apply the new interval if the work is already scheduled,
     * otherwise keep the existing one (safe to call on every app start)
     */
    fun startPeriodicChecks(
        intervalHours: Long = DEFAULT_CHECK_INTERVAL_HOURS,
        replaceExisting: Boolean = true,
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
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
            if (replaceExisting) ExistingPeriodicWorkPolicy.UPDATE else ExistingPeriodicWorkPolicy.KEEP,
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
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .addTag(ScheduleChangeWorker.WORK_TAG)
            .build()

        workManager.enqueueUniqueWork(
            ScheduleChangeWorker.IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest,
        )

        Timber.i("ScheduleChangeScheduler: Immediate check triggered")
    }

    /**
     * Emits true while a manually triggered check is waiting or running
     */
    fun observeImmediateCheckRunning(): Flow<Boolean> = workManager
        .getWorkInfosForUniqueWorkFlow(ScheduleChangeWorker.IMMEDIATE_WORK_NAME)
        .map { infos ->
            infos.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }
        }
        .distinctUntilChanged()

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
