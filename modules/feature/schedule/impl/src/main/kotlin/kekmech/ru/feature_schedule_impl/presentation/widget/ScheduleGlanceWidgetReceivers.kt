package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * One layout adapts itself to the size of the widget ([ScheduleGlanceWidget] is responsive); the receivers are
 * different sizes of the same widget in the launcher's widget list: 4x2, 2x1, 2x2 and 4x4.
 */
public abstract class BaseScheduleWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ScheduleGlanceWidget()

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        WidgetDayPrefs.clear(context, appWidgetIds)
    }
}

/** 4x2 */
public class ScheduleGlanceWidgetReceiver : BaseScheduleWidgetReceiver()

/** 2x1 */
public class ScheduleGlanceWidgetCompactReceiver : BaseScheduleWidgetReceiver()

/** 2x2 */
public class ScheduleGlanceWidgetSquareReceiver : BaseScheduleWidgetReceiver()

/** 4x3 / 4x4 */
public class ScheduleGlanceWidgetLargeReceiver : BaseScheduleWidgetReceiver()
