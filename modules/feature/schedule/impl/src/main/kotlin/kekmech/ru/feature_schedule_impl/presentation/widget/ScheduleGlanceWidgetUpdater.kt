package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

internal class ScheduleGlanceWidgetUpdater(
    private val context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) {

    fun update() {
        scope.launch {
            try {
                ScheduleGlanceWidget().updateAll(context)
            } catch (e: Throwable) {
                Timber.w(e, "Failed to update ScheduleGlanceWidget")
            }
        }
    }

    suspend fun updateAll() {
        try {
            ScheduleGlanceWidget().updateAll(context)
        } catch (e: Throwable) {
            Timber.w(e, "Failed to update ScheduleGlanceWidget")
        }
    }
}
