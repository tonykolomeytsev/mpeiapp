package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ext_kotlin.moscowLocalDate
import kekmech.ru.feature_schedule_api.domain.model.Classes
import kekmech.ru.feature_schedule_api.domain.model.Day
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val dayNames = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")
private val monthFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("ru"))

@Composable
internal fun WeeklyTimelineGrid(
    daysWithClasses: List<Pair<Day, List<Classes>>>,
    onClassesClick: (Classes) -> Unit,
    onDayHeaderClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = moscowLocalDate()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            items = daysWithClasses,
            key = { it.first.date.toString() },
        ) { (day, classes) ->
            val isToday = day.date == today
            val dayNameIndex = day.date.dayOfWeek.value - 1
            val dayName = dayNames.getOrElse(dayNameIndex) { "" }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Left Column: Day badge & Timeline
                Column(
                    modifier = Modifier
                        .width(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isToday) MpeixTheme.palette.primary.copy(alpha = 0.12f)
                            else MpeixTheme.palette.surfacePlus1
                        )
                        .border(
                            width = 1.dp,
                            color = if (isToday) MpeixTheme.palette.primary.copy(alpha = 0.5f)
                            else MpeixTheme.palette.outline.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                        )
                        .clickable { onDayHeaderClick(day.date) }
                        .padding(vertical = 6.dp, horizontal = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = dayName,
                        style = MpeixTheme.typography.labelNormal.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isToday) MpeixTheme.palette.primary else MpeixTheme.palette.contentVariant,
                            fontSize = 11.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        style = MpeixTheme.typography.header3.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isToday) MpeixTheme.palette.primary else MpeixTheme.palette.content,
                            fontSize = 15.sp,
                        ),
                    )
                    if (classes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isToday) MpeixTheme.palette.primary
                                    else MpeixTheme.palette.surfacePlus3
                                )
                                .padding(horizontal = 4.dp, vertical = 1.dp),
                        ) {
                            Text(
                                text = "${classes.size}",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isToday) MpeixTheme.palette.contentAccent else MpeixTheme.palette.content,
                                    fontSize = 9.sp,
                                ),
                            )
                        }
                    }
                }

                // Right Column: Classes for this day
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (classes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MpeixTheme.palette.surfacePlus1.copy(alpha = 0.5f))
                                .padding(vertical = 14.dp, horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(
                                text = "Нет занятий",
                                style = MpeixTheme.typography.paragraphNormal.copy(
                                    color = MpeixTheme.palette.contentDisabled,
                                    fontSize = 13.sp,
                                ),
                            )
                        }
                    } else {
                        classes.forEach { cls ->
                            ClassesItemCard(
                                classes = cls,
                                onClick = { onClassesClick(cls) },
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
