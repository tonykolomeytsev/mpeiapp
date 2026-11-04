package kekmech.ru.ui_kit_states

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # EmptyState
 *
 * Compose version of `EmptyStateItem` from coreui: centered title and subtitle
 * shown instead of the empty list.
 *
 * Usage example:
 * ```kotlin
 * EmptyState(
 *     title = stringResource(Strings.all_notes_empty_state_title),
 *     subtitle = stringResource(Strings.all_notes_empty_state_subtitle),
 * )
 * ```
 *
 * @param title the title of the empty state
 * @param subtitle the text displayed below the title
 * @param modifier [Modifier] to be applied to the empty state
 */
@Composable
public fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            style = MpeixTheme.typography.titleMedium,
            color = MpeixTheme.palette.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MpeixTheme.typography.titleSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@PreviewLightDark
private fun EmptyStatePreview() {
    MpeixPreview {
        EmptyState(
            title = "Нет ни одной актуальной заметки",
            subtitle = "Для создания заметки, нажмите на любую пару в разделе расписания",
            modifier = Modifier.background(MpeixTheme.palette.surface),
        )
    }
}
