package kekmech.ru.feature_bars_impl.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.icon.Update
import kekmech.ru.ui_theme.theme.MpeixTheme
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant
import kekmech.ru.res_strings.R.string as Strings

/**
 * "Обновлено 26 ноября, 14:35": when the data on screen was downloaded from the site.
 * Shown even when everything is fine, so that without internet the student can tell how old the data is.
 */
@Composable
internal fun UpdatedAtCaption(
    updatedAt: Instant,
    modifier: Modifier = Modifier,
) {
    val locale = LocalConfiguration.current.locales[0]
    val formatted = remember(updatedAt, locale) { formatUpdatedAt(updatedAt, locale) }
    // Looks like the time prediction on the dashboard
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Update,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MpeixTheme.palette.onSurface,
        )
        Text(
            text = stringResource(Strings.bars_updated_at, formatted),
            modifier = Modifier.padding(start = 8.dp),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurface,
        )
    }
}

/** "26 ноября, 14:35" like the saved schedule banner, with the year only when it is not the current one. */
internal fun formatUpdatedAt(
    updatedAt: Instant,
    locale: Locale,
    zoneId: ZoneId = ZoneId.systemDefault(),
    today: LocalDate = LocalDate.now(zoneId),
): String {
    val dateTime = java.time.Instant.ofEpochMilli(updatedAt.toEpochMilliseconds()).atZone(zoneId)
    val pattern = if (dateTime.year == today.year) "d MMMM, HH:mm" else "d MMMM yyyy, HH:mm"
    return dateTime.format(DateTimeFormatter.ofPattern(pattern, locale))
}

/** The older of the times: the data on screen is at least this fresh. Null when nothing is loaded yet. */
internal fun olderOf(vararg updatedAt: Instant?): Instant? = updatedAt.filterNotNull().minOrNull()

@PreviewLightDark
@Composable
private fun UpdatedAtCaptionPreview() {
    MpeixTheme {
        UpdatedAtCaption(
            updatedAt = Instant.parse("2026-11-26T11:35:00Z"),
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
