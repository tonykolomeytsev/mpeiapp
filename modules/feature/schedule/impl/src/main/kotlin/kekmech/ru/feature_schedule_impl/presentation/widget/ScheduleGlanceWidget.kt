package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

public class ScheduleGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = LocalDate.now()
        val data = loadTodayClasses(today)

        provideContent {
            GlanceTheme {
                ScheduleWidgetRoot(
                    context = context,
                    scheduleName = data.scheduleName,
                    today = today,
                    classes = data.classes,
                )
            }
        }
    }

    private suspend fun loadTodayClasses(today: LocalDate): WidgetScheduleData {
        return runCatching {
            val repository = getKoin().get<ScheduleRepository>()
            val selectedSchedule = repository.getSelectedSchedule()
            val schedule = repository.getSchedule(
                type = selectedSchedule.type,
                name = selectedSchedule.name,
                weekOffset = 0,
            ).getOrNull()

            val todayDay = schedule?.weeks
                ?.flatMap { it.days }
                ?.find { it.date == today }

            WidgetScheduleData(
                scheduleName = selectedSchedule.name,
                classes = todayDay?.classes.orEmpty(),
            )
        }.getOrElse {
            WidgetScheduleData(
                scheduleName = null,
                classes = emptyList(),
            )
        }
    }

    private data class WidgetScheduleData(
        val scheduleName: String?,
        val classes: List<Classes>,
    )
}

public class ScheduleGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ScheduleGlanceWidget()
}

@Composable
private fun ScheduleWidgetRoot(
    context: Context,
    scheduleName: String?,
    today: LocalDate,
    classes: List<Classes>,
) {
    val launchIntent = Intent().apply {
        component = ComponentName(context.packageName, "kekmech.ru.mpeiapp.MainActivity")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale("ru"))
    val formattedDate = today.format(dateFormatter)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(day = Color(0xFFF7F8FA), night = Color(0xFF1E1E1E)))
            .cornerRadius(16.dp)
            .padding(12.dp)
            .clickable(actionStartActivity(launchIntent)),
    ) {
        // Widget Header
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = scheduleName ?: "MpeiX",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(day = Color(0xFF1976D2), night = Color(0xFF90CAF9)),
                    ),
                    maxLines = 1,
                )
                Text(
                    text = formattedDate,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(day = Color(0xFF757575), night = Color(0xFFB0B0B0)),
                    ),
                    maxLines = 1,
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        // Widget Content
        if (scheduleName == null) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Откройте MpeiX, чтобы выбрать группу",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = ColorProvider(day = Color(0xFF757575), night = Color(0xFF9E9E9E)),
                    ),
                )
            }
        } else if (classes.isEmpty()) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Пар нет! Можно отдохнуть 🎉",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(day = Color(0xFF4CAF50), night = Color(0xFF81C784)),
                    ),
                )
            }
        } else {
            LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                items(classes) { item ->
                    WidgetClassRow(item)
                    Spacer(modifier = GlanceModifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
private fun WidgetClassRow(classes: Classes) {
    val timeStart = classes.time.start.format(DateTimeFormatter.ofPattern("HH:mm"))
    val timeEnd = classes.time.end.format(DateTimeFormatter.ofPattern("HH:mm"))
    val typeName = when (classes.type) {
        ClassesType.LECTURE -> "Лекция"
        ClassesType.PRACTICE -> "Практика"
        ClassesType.LAB -> "Лабораторная"
        ClassesType.COURSE -> "Курсовая"
        ClassesType.EXAM -> "Экзамен"
        ClassesType.CONSULTATION -> "Консультация"
        ClassesType.UNDEFINED -> classes.rawType ?: ""
    }

    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(ColorProvider(day = Color.White, night = Color(0xFF2C2C2C)))
            .cornerRadius(10.dp)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Time & Number
        Column(
            modifier = GlanceModifier.width(52.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = timeStart,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorProvider(day = Color(0xFF212121), night = Color.White),
                ),
            )
            Text(
                text = timeEnd,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = ColorProvider(day = Color(0xFF757575), night = Color(0xFF9E9E9E)),
                ),
            )
        }

        Spacer(modifier = GlanceModifier.width(8.dp))

        // Class details
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = classes.name,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ColorProvider(day = Color(0xFF212121), night = Color.White),
                ),
                maxLines = 1,
            )
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                if (classes.place.isNotBlank()) {
                    Text(
                        text = classes.place,
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = ColorProvider(day = Color(0xFF1976D2), night = Color(0xFF90CAF9)),
                        ),
                        maxLines = 1,
                    )
                    Spacer(modifier = GlanceModifier.width(6.dp))
                }
                if (typeName.isNotBlank()) {
                    Text(
                        text = "• $typeName",
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = ColorProvider(day = Color(0xFF757575), night = Color(0xFF9E9E9E)),
                        ),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
