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
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val today = moscowLocalDate()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        (0..5).forEach { dayOffset ->
            val date = firstDayOfWeek.plusDays(dayOffset.toLong())
            val isSelected = date == selectedDate
            val isToday = date == today

            DaySelectorItem(
                dayName = dayNames[dayOffset],
                dayNumber = date.dayOfMonth,
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
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayName,
            style = MpeixTheme.typography.paragraphSmall.copy(
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.contentVariant,
                fontSize = 12.sp,
            ),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dayNumber.toString(),
                style = MpeixTheme.typography.paragraphNormal.copy(
                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                    color = textColor,
                ),
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Small indicator dot for today
        if (isToday) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MpeixTheme.palette.primary),
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
