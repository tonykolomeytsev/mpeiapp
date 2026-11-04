package kekmech.ru.feature_dashboard_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_icons.R.drawable as Icons
import kekmech.ru.res_strings.R.string as Strings

/**
 * Fake search field, opens the search screen on click.
 */
@Composable
internal fun SearchField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MpeixTheme.palette.secondary)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Icons.ic_search_24),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MpeixTheme.palette.onSurfaceVariant,
        )
        Text(
            text = stringResource(Strings.dashboard_search_field_text),
            modifier = Modifier.padding(start = 16.dp),
            style = MpeixTheme.typography.bodyLarge,
            color = MpeixTheme.palette.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@PreviewLightDark
@Composable
private fun SearchFieldPreview() {
    MpeixTheme {
        SearchField(
            onClick = { /* no-op */ },
            modifier = Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp),
        )
    }
}
