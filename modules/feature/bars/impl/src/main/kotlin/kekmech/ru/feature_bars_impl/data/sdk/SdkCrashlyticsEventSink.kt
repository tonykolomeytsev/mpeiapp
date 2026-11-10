package kekmech.ru.feature_bars_impl.data.sdk

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.github.tonykolomeytsev.mpeix.sdk.SdkEvent
import io.github.tonykolomeytsev.mpeix.sdk.SdkEventSink
import io.github.tonykolomeytsev.mpeix.sdk.Severity

/**
 * Forwards SDK events to Crashlytics by their generic fields only,
 * so new event types need no changes here.
 */
internal class SdkCrashlyticsEventSink : SdkEventSink {

    private val crashlytics get() = FirebaseCrashlytics.getInstance()

    override fun onEvent(event: SdkEvent) {
        when (event.severity) {
            Severity.Info,
            Severity.Warning -> crashlytics.log("[mpeix-sdk] ${event.name} ${event.diagnosticId.value}")

            Severity.Error,
            Severity.Fatal -> {
                event.attributes.forEach { (key, value) -> crashlytics.setCustomKey(key, value) }
                crashlytics.setCustomKey("diagnostic_id", event.diagnosticId.value)
                crashlytics.recordException(event.error ?: SdkEventException(event))
            }
        }
    }

    private class SdkEventException(event: SdkEvent) :
        RuntimeException("${event.name} (${event.diagnosticId.value})")
}
