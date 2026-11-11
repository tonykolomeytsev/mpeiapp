package kekmech.ru.feature_schedule_impl.sync

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Keeps the nightly schedule sync scheduled */
internal class ScheduleSyncInitializer(
    private val scheduleSyncScheduler: ScheduleSyncSchedulerImpl,
) : AppLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Suppress("TooGenericExceptionCaught")
    override fun onCreate(context: Context) {
        scope.launch {
            try {
                scheduleSyncScheduler.scheduleNightlySync()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}
