package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.TwemojiSparkles
import kekmech.ru.feature_dashboard_impl.R
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

/**
 * Clickable card with the text and the illustration peeking from the bottom end corner.
 *
 * @param title the title of the banner
 * @param illustration the illustration in the bottom end corner
 * @param illustrationSize size of the [illustration]
 * @param description the optional text displayed below the title
 */
@Composable
internal fun DashboardBanner(
    title: String,
    @DrawableRes illustration: Int,
    illustrationSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MpeixTheme.palette.secondary)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 128.dp, top = 12.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MpeixTheme.typography.titleMedium,
                color = MpeixTheme.palette.onSurface,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.onSurfaceVariant,
                )
            }
        }
        Image(
            painter = painterResource(illustration),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 12.dp)
                .size(illustrationSize),
        )
    }
}

@Composable
internal fun LunchBanner(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DashboardBanner(
        title = stringResource(Strings.dashboard_banner_lunch_header),
        description = stringResource(Strings.dashboard_banner_lunch_description),
        illustration = R.drawable.ic_banner_lunch,
        illustrationSize = 100.dp,
        onClick = onClick,
        modifier = modifier,
    )
}

/**
 * Banner leading to the "What's new" screen. Can be dismissed with the close button.
 */
@Composable
internal fun WhatsNewBanner(
    onClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MpeixTheme.palette.secondary)
            .clickable(onClick = onClick),
    ) {
        Row(
            // the end padding leaves room for the close button
            modifier = Modifier.padding(start = 16.dp, end = 40.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = stringResource(Strings.dashboard_banner_whats_new_header),
                    style = MpeixTheme.typography.titleMedium,
                    color = MpeixTheme.palette.onSurface,
                )
                Text(
                    text = stringResource(Strings.dashboard_banner_whats_new_description),
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.onSurfaceVariant,
                )
            }
            Image(
                imageVector = TwemojiSparkles,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(48.dp),
            )
        }
        IconButton(
            onClick = onDismissClick,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = MpeixTheme.palette.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun DashboardBannerPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LunchBanner(onClick = { /* no-op */ })
            WhatsNewBanner(onClick = { /* no-op */ }, onDismissClick = { /* no-op */ })
        }
    }
}
