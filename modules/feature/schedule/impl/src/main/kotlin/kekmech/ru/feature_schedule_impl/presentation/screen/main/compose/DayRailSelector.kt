package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate

private val dayNames = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")

@Composable
internal fun DayRailSelector(
    firstDayOfWeek: LocalDate,
    selectedDate: LocalDate,
    classesCountByDate: Map<LocalDate, Int>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = moscowLocalDate()

    Column(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(MpeixTheme.palette.surfacePlus1.copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                color = MpeixTheme.palette.outline.copy(alpha = 0.12f),
            )
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp, horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Mon through Sat (plus Sun if it has classes)
        val maxDays = if (classesCountByDate[firstDayOfWeek.plusDays(6)] ?: 0 > 0) 6 else 5
        (0..maxDays).forEach { dayOffset ->
            val date = firstDayOfWeek.plusDays(dayOffset.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today
            val count = classesCountByDate[date] ?: 0

            DayRailItem(
                dayName = dayNames.getOrElse(dayOffset) { "" },
                dayNumber = date.dayOfMonth,
                classesCount = count,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onDayClick(date) },
            )
        }
    }
}

@Composable
private fun DayRailItem(
    dayName: String,
    dayNumber: Int,
    classesCount: Int,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
) {
    val backgroundColor = when {
        isSelected -> MpeixTheme.palette.primary
        isToday -> MpeixTheme.palette.primary.copy(alpha = 0.12f)
        else -> Color.Transparent
    }

    val contentColor = when {
        isSelected -> MpeixTheme.palette.contentAccent
        isToday -> MpeixTheme.palette.primary
        else -> MpeixTheme.palette.content
    }

    val secondaryColor = when {
        isSelected -> MpeixTheme.palette.contentAccent.copy(alpha = 0.8f)
        isToday -> MpeixTheme.palette.primary
        else -> MpeixTheme.palette.contentVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                width = if (isToday && !isSelected) 1.dp else 0.dp,
                color = if (isToday && !isSelected) MpeixTheme.palette.primary.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Day name (ПН, ВТ, ...)
        Text(
            text = dayName,
            style = MpeixTheme.typography.labelNormal.copy(
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                color = secondaryColor,
                fontSize = 11.sp,
            ),
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Day number (12, 13, ...)
        Text(
            text = dayNumber.toString(),
            style = MpeixTheme.typography.header3.copy(
                fontWeight = FontWeight.Bold,
                color = contentColor,
                fontSize = 17.sp,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Classes count badge or dot
        if (classesCount > 0) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (isSelected) MpeixTheme.palette.surfacePlus1.copy(alpha = 0.35f)
                        else MpeixTheme.palette.surfacePlus3
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "$classesCount",
                    style = MpeixTheme.typography.paragraphNormal.copy(
                        fontWeight = FontWeight.Bold,
                        color = contentColor,
                        fontSize = 10.sp,
                    ),
                )
            }
        } else {
            // Muted placeholder dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MpeixTheme.palette.contentAccent.copy(alpha = 0.5f)
                        else MpeixTheme.palette.outline.copy(alpha = 0.3f)
                    ),
            )
        }
    }
}
