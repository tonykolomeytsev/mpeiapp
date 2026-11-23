package kekmech.ru.feature_bars_impl.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Daily background refresh of BARS, see [SyncBarsUseCase] and [BarsSyncScheduler].
 */
internal class BarsSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val syncBars: SyncBarsUseCase by inject()
    private val barsSyncScheduler: BarsSyncScheduler by inject()

    override suspend fun doWork(): Result {
        val synced = syncBars()
        if (!synced && runAttemptCount + 1 < MaxAttempts) return Result.retry()
        // Only now: a moved run would also delay the retry until tomorrow
        moveNextRun()
        // give up until tomorrow, the user still refreshes what they open
        return if (synced) Result.success() else Result.failure()
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun moveNextRun() {
        try {
            barsSyncScheduler.moveNextRun(id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // the next run comes in 24 hours instead, still daily
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private companion object {

        const val MaxAttempts = 3
    }
}
