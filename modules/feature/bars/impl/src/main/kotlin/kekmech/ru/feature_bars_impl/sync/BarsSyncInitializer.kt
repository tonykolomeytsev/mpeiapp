package kekmech.ru.feature_bars_impl.sync

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.tonykolomeytsev.mpeix.sdk.AccountStatus
import io.github.tonykolomeytsev.mpeix.sdk.MpeixSdk
import kekmech.ru.lib_app_lifecycle.AppLifecycleObserver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Keeps the daily BARS refresh scheduled while some account is signed in, and cancels it when none is:
 * users who never sign in get no background work at all.
 *
 * @param sdk lazy: the SDK is created off the main thread, not in `Application.onCreate`
 */
internal class BarsSyncInitializer(
    private val sdk: Lazy<MpeixSdk>,
    private val barsSyncScheduler: BarsSyncScheduler,
) : AppLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Suppress("TooGenericExceptionCaught")
    override fun onCreate(context: Context) {
        scope.launch {
            try {
                // The SDK re-checks the session cookies for every new collector, so a session that expired
                // while the app wasn't running is seen here at the next start
                sdk.value.bars.auth.accounts
                    .map { accounts -> accounts.any { it.status == AccountStatus.SignedIn } }
                    .distinctUntilChanged()
                    .collect { hasSession ->
                        if (hasSession) barsSyncScheduler.ensureScheduled() else barsSyncScheduler.cancel()
                    }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}
