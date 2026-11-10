package kekmech.ru.feature_bars_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_bars_impl.presentation.screen.main.compose.icon.CloudOff
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private val BannerShape = RoundedCornerShape(12.dp)

/** Why the data on the screen is the cached one, not fresh from the site. The banner shows them in this order. */
internal enum class ShowingFromCacheReason {
    /** Any error except the network and the session ones, e.g. the page could not be parsed. */
    ProcessingFailed,
    SessionExpired,
    NetworkError,
}

/**
 * Small gray banner above the marks when they are shown from the cache.
 * The title is always the same, below it there is one line for each of the [reasons].
 *
 * @param onClick null makes the banner not clickable
 */
@Composable
internal fun ShowingFromCacheBanner(
    reasons: Set<ShowingFromCacheReason>,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .adaptiveContentWidth()
            .padding(horizontal = 16.dp)
            .background(MpeixTheme.palette.surfaceContainer, BannerShape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Icon(
            imageVector = CloudOff,
            contentDescription = null,
            tint = MpeixTheme.palette.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(Strings.bars_showing_from_cache),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurface,
            )
            reasons.sorted().forEach { reason ->
                Text(
                    text = stringResource(reason.text),
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.onSurfaceVariant,
                )
            }
        }
    }
}

private val ShowingFromCacheReason.text: Int
    get() = when (this) {
        ShowingFromCacheReason.ProcessingFailed -> Strings.bars_showing_from_cache_processing_failed
        ShowingFromCacheReason.SessionExpired -> Strings.bars_showing_from_cache_logged_out
        ShowingFromCacheReason.NetworkError -> Strings.bars_showing_from_cache_network
    }

@PreviewLightDark
@Composable
private fun ShowingFromCacheBannerAllReasonsPreview() {
    MpeixTheme {
        ShowingFromCacheBanner(reasons = ShowingFromCacheReason.entries.toSet())
    }
}

@PreviewLightDark
@Composable
private fun ShowingFromCacheBannerNetworkPreview() {
    MpeixTheme {
        ShowingFromCacheBanner(reasons = setOf(ShowingFromCacheReason.NetworkError))
    }
}
