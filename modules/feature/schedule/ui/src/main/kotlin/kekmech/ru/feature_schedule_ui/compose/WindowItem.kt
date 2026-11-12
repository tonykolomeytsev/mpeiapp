package kekmech.ru.feature_schedule_ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

/**
 * # WindowItem
 *
 * Label of the gap between two classes, when there are skipped classes numbers between them.
 *
 * Replaces the `WindowAdapterItem` of the RecyclerView implementation.
 *
 * @param modifier [Modifier] to be applied to the item
 */
@Composable
public fun WindowItem(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(Strings.schedule_item_window_header),
        style = MpeixTheme.typography.titleMedium,
        color = MpeixTheme.palette.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp),
    )
}

@PreviewLightDark
@Composable
private fun WindowItemPreview() {
    MpeixTheme {
        WindowItem(Modifier.background(MpeixTheme.palette.surface))
    }
}
