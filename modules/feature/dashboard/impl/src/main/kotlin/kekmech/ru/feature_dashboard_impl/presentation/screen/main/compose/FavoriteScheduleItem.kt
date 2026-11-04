package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Favorite schedule, selects it on click.
 *
 * @param isSelected true if the schedule is the selected one, it is highlighted then
 */
@Composable
internal fun FavoriteScheduleItem(
    name: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MpeixTheme.palette.primary else MpeixTheme.palette.secondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = name,
            style = MpeixTheme.typography.bodyLarge,
            color = if (isSelected) MpeixTheme.palette.onPrimary else MpeixTheme.palette.onSurface,
        )
        Text(
            text = description,
            style = MpeixTheme.typography.labelMedium,
            fontSize = 13.sp,
            color = if (isSelected) {
                MpeixTheme.palette.onPrimary.copy(alpha = 0.8f)
            } else {
                MpeixTheme.palette.onSurfaceVariant
            },
        )
    }
}

@PreviewLightDark
@Composable
private fun FavoriteScheduleItemPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FavoriteScheduleItem(
                name = "С-12-16",
                description = "Моя группа",
                isSelected = true,
                onClick = { /* no-op */ },
            )
            FavoriteScheduleItem(
                name = "А-08-19",
                description = "Группа друга",
                isSelected = false,
                onClick = { /* no-op */ },
            )
        }
    }
}
