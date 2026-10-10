package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate

private val dayNames = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ")

@Composable
internal fun WeekDaySelector(
    firstDayOfWeek: LocalDate,
    selectedDate: LocalDate,
    classesCountByDate: Map<LocalDate, Int>? = null,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = moscowLocalDate()
    val hasSundayClasses = (classesCountByDate?.get(firstDayOfWeek.plusDays(6)) ?: 0) > 0
    val daysCount = if (hasSundayClasses) 7 else 6

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        (0 until daysCount).forEach { dayOffset ->
            val date = firstDayOfWeek.plusDays(dayOffset.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today
            val count = classesCountByDate?.get(date)

            DaySelectorItem(
                dayName = dayNames.getOrElse(dayOffset) { "" },
                dayNumber = date.dayOfMonth,
                classesCount = count,
                isSelected = isSelected,
                isToday = isToday,
                onClick = { onDayClick(date) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DaySelectorItem(
    dayName: String,
    dayNumber: Int,
    classesCount: Int?,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (isSelected) {
        MpeixTheme.palette.primary
    } else {
        Color.Transparent
    }

    val textColor = when {
        isSelected -> MpeixTheme.palette.contentAccent
        isToday -> MpeixTheme.palette.primary
        else -> MpeixTheme.palette.content
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayName,
            style = MpeixTheme.typography.labelNormal.copy(
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.contentVariant,
                fontSize = 11.sp,
            ),
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dayNumber.toString(),
                style = MpeixTheme.typography.paragraphNormal.copy(
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                    fontSize = 13.sp,
                ),
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Indicator dot for today or class count
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.primary),
            )
        } else if (classesCount != null && classesCount > 0) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.primary.copy(alpha = 0.5f)),
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
