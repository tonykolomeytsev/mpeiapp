package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback

internal val ShiftDaysKey = ActionParameters.Key<Int>("shift_days")

/**
 * "<" and ">" of the widget: moves the shown day without opening the app.
 * The click is handled by the widget host, only the widget itself is redrawn.
 */
public class ShiftDayCallback : ActionCallback {

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val delta = parameters[ShiftDaysKey] ?: return
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        WidgetDayPrefs.shift(context, appWidgetId, delta)
        ScheduleGlanceWidget().update(context, glanceId)
    }
}

/** "Today": back to the current day */
public class ShowTodayCallback : ActionCallback {

    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
        WidgetDayPrefs.reset(context, appWidgetId)
        ScheduleGlanceWidget().update(context, glanceId)
    }
}
