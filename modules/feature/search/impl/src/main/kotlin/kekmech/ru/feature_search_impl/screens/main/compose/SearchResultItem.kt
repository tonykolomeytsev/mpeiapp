package kekmech.ru.feature_search_impl.screens.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_icons.R.drawable as IconsRes

/**
 * Search result with a small leading icon, aligned with the first line of the [title].
 *
 * @param title name of the result
 * @param subtitle details of the result, not shown if blank
 * @param icon type of the result
 * @param onClick called when the result is clicked
 */
@Composable
internal fun SearchResultItem(
    title: String,
    subtitle: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleStyle = MpeixTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
    // Icon is centered in the first line of the title, so it stays there when the title wraps
    val titleLineHeight = with(LocalDensity.current) { titleStyle.lineHeight.toDp() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier.height(titleLineHeight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSize)
                    .offset(y = (-2).dp), // fine-tuning
                tint = MpeixTheme.palette.onSurfaceVariant,
            )
        }
        Column(Modifier.padding(start = 12.dp)) {
            Text(
                text = title,
                style = titleStyle,
                color = MpeixTheme.palette.onSurface,
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MpeixTheme.typography.labelMedium,
                    color = MpeixTheme.palette.onSurfaceVariant,
                )
            }
        }
    }
}

private val IconSize = 16.dp

@PreviewLightDark
@Composable
private fun SearchResultItemPreview() {
    MpeixTheme {
        Column(Modifier.background(MpeixTheme.palette.surface)) {
            SearchResultItem(
                title = "Корпус Т",
                subtitle = "Красноказарменная ул., 13с4",
                icon = rememberVectorPainter(Icons.Filled.LocationOn),
                onClick = { /* no-op */ },
            )
            SearchResultItem(
                title = "А-02м-23",
                subtitle = "Институт информационных и вычислительных технологий | Очная",
                icon = painterResource(IconsRes.ic_groups_black_24),
                onClick = { /* no-op */ },
            )
            SearchResultItem(
                title = "Бободжанов Абдухафиз Абдурасулович",
                subtitle = "Кафедра ТМ",
                icon = painterResource(IconsRes.ic_person_24),
                onClick = { /* no-op */ },
            )
            SearchResultItem(
                title = "Столовая",
                subtitle = "",
                icon = rememberVectorPainter(Icons.Filled.LocationOn),
                onClick = { /* no-op */ },
            )
        }
    }
}
