package kekmech.ru.feature_schedule_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private val BannerShape = RoundedCornerShape(12.dp)

/**
 * Banner shown when the schedule failed to update and the saved schedule is shown instead.
 *
 * @param text banner text with the date of the saved schedule
 * @param onClick called when the banner is clicked, it should refresh the schedule
 * @param modifier [Modifier] to be applied to the banner
 */
@Composable
internal fun SavedScheduleBanner(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(BannerShape)
            .background(MpeixTheme.palette.secondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = stringResource(Strings.schedule_saved_banner_refresh),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.primary,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@PreviewLightDark
@Composable
private fun SavedScheduleBannerPreview() {
    MpeixTheme {
        SavedScheduleBanner(
            text = "Не удалось обновить.\nРасписание от 10 ноября, 14:20",
            onClick = {},
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(8.dp),
        )
    }
}
