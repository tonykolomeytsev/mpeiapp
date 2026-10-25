package kekmech.ru.feature_schedule_impl.presentation.widget

import android.content.ComponentName
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import kekmech.ru.ext_android.AppLocales
import kekmech.ru.feature_schedule_api.data.repository.ScheduleRepository
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.ClassesType
import kekmech.ru.feature_schedule_impl.domain.ClassTimeline
import org.koin.java.KoinJavaComponent.getKoin
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kekmech.ru.res_strings.R.string as Strings

/**
 * Schedule widget. One responsive layout for all sizes:
 * - 2x1: the nearest class in two lines;
 * - 2x2: the nearest class on a card with its time, room and type;
 * - 4x2: day switcher and the next two classes;
 * - 4x3, 4x4: day switcher and the whole day.
 * The buttons `<`, `>` and "Today" change the shown day right in the widget (see [ShiftDayCallback]).
 */
public class ScheduleGlanceWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Responsive(setOf(CompactSize, SquareSize, MediumSize, LargeSize))

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val today = LocalDate.now()
        val offset = WidgetDayPrefs.offset(context, appWidgetId, today)
        val data = loadDay(today.plusDays(offset.toLong()), today)

        provideContent {
            GlanceTheme {
                ScheduleWidgetRoot(
                    context = AppLocales.localized(context),
                    locale = AppLocales.localeOf(context),
                    data = data,
                    isToday = offset == 0,
                    launchComponent = ComponentName(context.packageName, MAIN_ACTIVITY),
                )
            }
        }
    }

    private suspend fun loadDay(date: LocalDate, today: LocalDate): WidgetDay =
        runCatching {
            val repository = getKoin().get<ScheduleRepository>()
            val selected = repository.getSelectedSchedule()
            val schedule = repository.getSchedule(
                type = selected.type,
                name = selected.name,
                weekOffset = WidgetDayPrefs.weekOffset(today, date),
            ).getOrNull()
            val day = schedule?.weeks?.flatMap { it.days }?.find { it.date == date }
            val classes = day?.classes.orEmpty().sortedBy { it.time.start }
            WidgetDay(
                scheduleName = selected.name,
                date = date,
                classes = classes,
                // the "now / next" view only makes sense for the current day
                timeline = if (date == today) ClassTimeline.of(classes, LocalDateTime.now().toLocalTime()) else null,
                isLoaded = schedule != null,
            )
        }.getOrElse {
            WidgetDay(scheduleName = null, date = date, classes = emptyList(), timeline = null, isLoaded = false)
        }

    internal companion object {
        const val MAIN_ACTIVITY = "kekmech.ru.mpeiapp.MainActivity"

        // 70 * cells - 30 dp is the size a launcher gives to a widget of that many cells
        val CompactSize = DpSize(110.dp, 40.dp)
        val SquareSize = DpSize(110.dp, 110.dp)
        val MediumSize = DpSize(250.dp, 110.dp)
        val LargeSize = DpSize(250.dp, 180.dp)
    }
}

/** What the widget shows */
internal data class WidgetDay(
    /** The selected group / person, null if nothing is selected yet */
    val scheduleName: String?,
    val date: LocalDate,
    val classes: List<Classes>,
    /** Only for today */
    val timeline: ClassTimeline?,
    /** false while the schedule could not be loaded (no network and no cache) */
    val isLoaded: Boolean,
)

private val BackgroundColor = ColorProvider(day = Color(0xFFF7F8FA), night = Color(0xFF1E1E1E))
private val CardColor = ColorProvider(day = Color.White, night = Color(0xFF2C2C2C))
private val CurrentCardColor = ColorProvider(day = Color(0xFFE3F2FD), night = Color(0xFF263B52))
private val AccentColor = ColorProvider(day = Color(0xFF1976D2), night = Color(0xFF90CAF9))
private val PrimaryText = ColorProvider(day = Color(0xFF212121), night = Color.White)
private val SecondaryText = ColorProvider(day = Color(0xFF757575), night = Color(0xFF9E9E9E))
private val RestColor = ColorProvider(day = Color(0xFF4CAF50), night = Color(0xFF81C784))

@Composable
private fun ScheduleWidgetRoot(
    context: Context,
    locale: Locale,
    data: WidgetDay,
    isToday: Boolean,
    launchComponent: ComponentName,
) {
    val size = LocalSize.current
    val openApp = actionStartActivity(launchComponent)
    val compact = size.height < SquareSize.height
    val square = !compact && size.width < MediumSize.width
    val medium = !compact && !square && size.height < LargeSize.height

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(BackgroundColor)
            .cornerRadius(16.dp)
            .padding(if (compact) 8.dp else 12.dp)
            .clickable(openApp),
    ) {
        when {
            data.scheduleName == null -> Message(
                text = context.getString(Strings.schedule_widget_select_group, context.getString(Strings.brand_name)),
                color = SecondaryText,
            )

            compact -> CompactContent(context, data)
            square -> SquareContent(context, data)
            else -> DayContent(context, locale, data, isToday, limit = if (medium) MEDIUM_LIMIT else null)
        }
    }
}

@Composable
private fun Message(text: String, color: ColorProvider, fontSize: Int = 13) {
    Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Medium,
                color = color,
                textAlign = TextAlign.Center,
            ),
            maxLines = 3,
        )
    }
}

/** 2x1: the current (or the next) class in two lines */
@Composable
private fun CompactContent(context: Context, data: WidgetDay) {
    val focus = data.timeline?.focus ?: data.classes.firstOrNull()
    if (focus == null) {
        Message(context.emptyDayText(data), RestColor, fontSize = 12)
        return
    }
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = focus.name,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryText),
            maxLines = 1,
        )
        Text(
            text = listOf(focus.timeRange(), focus.place.takeIf { it.isNotBlank() }).filterNotNull().joinToString(" · "),
            style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = AccentColor),
            maxLines = 1,
        )
    }
}

/** 2x2: card of the current (or the next) class */
@Composable
private fun SquareContent(context: Context, data: WidgetDay) {
    val timeline = data.timeline
    val focus = timeline?.focus ?: data.classes.firstOrNull()
    if (focus == null) {
        Message(context.emptyDayText(data), RestColor, fontSize = 13)
        return
    }
    val label = when {
        timeline == null -> null
        timeline.current != null -> context.getString(Strings.widget_now)
        else -> context.getString(Strings.widget_next)
    }
    Column(modifier = GlanceModifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
        if (label != null) {
            Text(
                text = label.uppercase(),
                style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SecondaryText),
                maxLines = 1,
            )
        }
        Text(
            text = focus.timeRange(),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AccentColor),
            maxLines = 1,
        )
        Text(
            text = focus.name,
            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = PrimaryText),
            maxLines = 2,
        )
        Spacer(modifier = GlanceModifier.height(2.dp))
        Text(
            text = listOf(focus.place.takeIf { it.isNotBlank() }, context.typeName(focus).takeIf { it.isNotBlank() })
                .filterNotNull()
                .joinToString(" · "),
            style = TextStyle(fontSize = 11.sp, color = SecondaryText),
            maxLines = 1,
        )
    }
}

/** 4x2, 4x3, 4x4: day switcher and the list of classes */
@Composable
private fun DayContent(context: Context, locale: Locale, data: WidgetDay, isToday: Boolean, limit: Int?) {
    val timeline = data.timeline
    // for today the classes that are already over are hidden in the small list, the large one shows the whole day
    val shown = if (limit != null && timeline != null) timeline.remaining else data.classes
    val items = if (limit != null) shown.take(limit) else shown
    val hidden = shown.size - items.size

    Column(modifier = GlanceModifier.fillMaxSize()) {
        DayHeader(context, locale, data, isToday)
        Spacer(modifier = GlanceModifier.height(6.dp))
        when {
            data.classes.isEmpty() -> Message(context.emptyDayText(data), RestColor, fontSize = 14)
            items.isEmpty() -> Message(context.getString(Strings.widget_classes_over), RestColor, fontSize = 14)
            limit != null -> Column(modifier = GlanceModifier.fillMaxSize()) {
                items.forEach { item ->
                    WidgetClassRow(context, item, isCurrent = timeline?.current == item)
                    Spacer(modifier = GlanceModifier.height(4.dp))
                }
                if (hidden > 0) {
                    Text(
                        text = "+$hidden",
                        style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = SecondaryText),
                    )
                }
            }

            else -> LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                items(items) { item ->
                    Column {
                        WidgetClassRow(context, item, isCurrent = timeline?.current == item)
                        Spacer(modifier = GlanceModifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DayHeader(context: Context, locale: Locale, data: WidgetDay, isToday: Boolean) {
    val title = data.date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM", locale))
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
    Row(modifier = GlanceModifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        NavButton(
            symbol = "‹",
            description = context.getString(Strings.widget_prev_day),
            delta = -1,
        )
        Column(modifier = GlanceModifier.defaultWeight().padding(horizontal = 4.dp)) {
            Text(
                text = title,
                style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AccentColor),
                maxLines = 1,
            )
            Text(
                text = data.scheduleName ?: context.getString(Strings.brand_name),
                style = TextStyle(fontSize = 11.sp, color = SecondaryText),
                maxLines = 1,
            )
        }
        if (!isToday) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = GlanceModifier
                    .height(NAV_SIZE)
                    .padding(horizontal = 10.dp)
                    .background(CardColor)
                    .cornerRadius(NAV_SIZE)
                    .clickable(actionRunCallback<ShowTodayCallback>()),
            ) {
                Text(
                    text = context.getString(Strings.today),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, color = AccentColor),
                    maxLines = 1,
                )
            }
            Spacer(modifier = GlanceModifier.width(4.dp))
        }
        NavButton(
            symbol = "›",
            description = context.getString(Strings.widget_next_day),
            delta = 1,
        )
    }
}

@Composable
private fun NavButton(symbol: String, description: String, delta: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .size(NAV_SIZE)
            .background(CardColor)
            .cornerRadius(NAV_SIZE)
            .semantics { contentDescription = description }
            .clickable(actionRunCallback<ShiftDayCallback>(actionParametersOf(ShiftDaysKey to delta))),
    ) {
        Text(
            text = symbol,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AccentColor),
        )
    }
}

@Composable
private fun WidgetClassRow(context: Context, classes: Classes, isCurrent: Boolean) {
    val typeName = context.typeName(classes)
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .background(if (isCurrent) CurrentCardColor else CardColor)
            .cornerRadius(10.dp)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = GlanceModifier.width(52.dp), horizontalAlignment = Alignment.Start) {
            Text(
                text = classes.time.start.format(TIME),
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryText),
            )
            Text(
                text = classes.time.end.format(TIME),
                style = TextStyle(fontSize = 10.sp, color = SecondaryText),
            )
        }
        Spacer(modifier = GlanceModifier.width(8.dp))
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = classes.name,
                style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PrimaryText),
                maxLines = 1,
            )
            Row(modifier = GlanceModifier.fillMaxWidth()) {
                if (classes.place.isNotBlank()) {
                    Text(
                        text = classes.place,
                        style = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AccentColor),
                        maxLines = 1,
                    )
                    Spacer(modifier = GlanceModifier.width(6.dp))
                }
                if (typeName.isNotBlank()) {
                    Text(
                        text = "• $typeName",
                        style = TextStyle(fontSize = 10.sp, color = SecondaryText),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

private fun Classes.timeRange(): String = "${time.start.format(TIME)}–${time.end.format(TIME)}"

private fun Context.emptyDayText(data: WidgetDay): String =
    getString(if (data.isLoaded) Strings.widget_no_classes else Strings.widget_classes_unavailable)

private fun Context.typeName(classes: Classes): String =
    when (classes.type) {
        ClassesType.LECTURE -> getString(Strings.widget_type_lecture)
        ClassesType.PRACTICE -> getString(Strings.widget_type_practice)
        ClassesType.LAB -> getString(Strings.widget_type_lab)
        ClassesType.COURSE -> getString(Strings.widget_type_course)
        ClassesType.EXAM -> getString(Strings.widget_type_exam)
        ClassesType.CONSULTATION -> getString(Strings.widget_type_consultation)
        ClassesType.UNDEFINED -> classes.rawType.orEmpty()
    }

private const val MEDIUM_LIMIT = 2
private val NAV_SIZE = 32.dp
private val TIME: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
