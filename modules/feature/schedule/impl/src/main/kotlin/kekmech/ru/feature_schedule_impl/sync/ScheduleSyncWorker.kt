package kekmech.ru.feature_schedule_impl.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Background download of the selected and favorite schedules, see [ScheduleSyncSchedulerImpl].
 *
 * Downloads run in the application scope (see `CachedScheduleSource`), so if WorkManager stops
 * the worker, the downloads already started still finish and land in the cache.
 */
internal class ScheduleSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val syncSchedules: SyncSchedulesUseCase by inject()

    override suspend fun doWork(): Result =
        when {
            syncSchedules() -> Result.success()
            runAttemptCount + 1 < MaxAttempts -> Result.retry()
            // give up until the next sync, the foreground still refreshes what the user opens
            else -> Result.failure()
        }

    private companion object {

        const val MaxAttempts = 3
    }
}
