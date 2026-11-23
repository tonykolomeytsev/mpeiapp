package kekmech.ru.feature_bars_impl.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID

/**
 * The daily refresh is a periodic work whose every run is moved to a random moment of [BarsSyncWindow].
 * A periodic work never ends by itself, so the refresh keeps going for users who don't open the app at all.
 * If moving a run fails, WorkManager runs the work 24 hours after the previous one, which is still daily.
 */
internal class BarsSyncScheduler(
    private val context: Context,
) {

    private val workManager: WorkManager get() = WorkManager.getInstance(context)

    /** Schedules the refresh unless it's scheduled already: an app start must not move the planned moment. */
    fun ensureScheduled() {
        workManager.enqueueUniquePeriodicWork(DailyWorkName, ExistingPeriodicWorkPolicy.KEEP, dailyRequest().build())
    }

    fun cancel() {
        workManager.cancelUniqueWork(DailyWorkName)
    }

    /**
     * Picks a new moment for the next run of the running [workId]. Called from the run itself, the run isn't
     * interrupted. A cancelled work stays cancelled.
     */
    suspend fun moveNextRun(workId: UUID) {
        val request = dailyRequest().setId(workId).build()
        withContext(Dispatchers.IO) { workManager.updateWork(request).get() }
    }

    private fun dailyRequest(): PeriodicWorkRequest.Builder =
        PeriodicWorkRequestBuilder<BarsSyncWorker>(Duration.ofDays(1))
            .setNextScheduleTimeOverride(BarsSyncWindow.nextRun(ZonedDateTime.now()).toEpochMilli())
            .setConstraints(
                Constraints.Builder()
                    // not UNMETERED: many students only have mobile data, and it's two pages
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMinutes(15))

    private companion object {

        const val DailyWorkName = "bars_sync_daily"
    }
}
