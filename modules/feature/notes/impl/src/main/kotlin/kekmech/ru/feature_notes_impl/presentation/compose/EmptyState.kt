package kekmech.ru.feature_notes_impl.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

@Composable
internal fun EmptyState(
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
