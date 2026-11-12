package kekmech.ru.feature_schedule_impl.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kekmech.ru.feature_schedule_api.ScheduleSyncScheduler
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import kotlin.random.Random

internal class ScheduleSyncSchedulerImpl(
    private val context: Context,
) : ScheduleSyncScheduler {

    private val workManager: WorkManager get() = WorkManager.getInstance(context)

    override fun requestSync() {
        workManager.enqueueUniqueWork(
            SyncNowWorkName,
            // the latest request wins: it sees the latest selected and favorite schedules
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<ScheduleSyncWorker>()
                .setConstraints(networkConstraints(requiresBatteryNotLow = false))
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(1))
                .build(),
        )
    }

    /**
     * Make sure the nightly sync is scheduled, and scheduled at night: WorkManager counts the
     * next period from the previous run, so the sync drifts later day by day. Called on every
     * app start, reschedules only if needed.
     */
    suspend fun scheduleNightlySync() {
        val current = workManager
            .getWorkInfosForUniqueWorkFlow(NightlyWorkName)
            .first()
            .firstOrNull { !it.state.isFinished }
        if (current != null && current.isOnTrack()) return
        workManager.enqueueUniquePeriodicWork(
            NightlyWorkName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            PeriodicWorkRequestBuilder<ScheduleSyncWorker>(Duration.ofDays(1))
                .setInitialDelay(nextNightlyDelay())
                .setConstraints(networkConstraints(requiresBatteryNotLow = true))
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(15))
                .build(),
        )
    }

    private fun WorkInfo.isOnTrack(): Boolean =
        state == WorkInfo.State.RUNNING ||
            runAttemptCount > 0 || // retrying after a failure, let it finish
            NightlySyncWindow.contains(Instant.ofEpochMilli(nextScheduleTimeMillis))

    private fun nextNightlyDelay(): Duration {
        val jitter = Duration.ofMinutes(Random.nextLong(NightlySyncWindow.Spread.toMinutes()))
        return NightlySyncWindow.delayUntilNextStart(ZonedDateTime.now()).plus(jitter)
    }

    private fun networkConstraints(requiresBatteryNotLow: Boolean): Constraints =
        Constraints.Builder()
            // not UNMETERED: many students only have mobile data, and the schedule is tiny
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(requiresBatteryNotLow)
            .build()

    private companion object {

        const val SyncNowWorkName = "schedule_sync_now"
        const val NightlyWorkName = "schedule_sync_nightly"
    }
}
