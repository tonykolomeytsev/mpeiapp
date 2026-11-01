package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Explanation of the setting above it.
 */
@Composable
internal fun Description(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        style = MpeixTheme.typography.labelMedium,
        color = MpeixTheme.palette.onSurfaceVariant,
    )
}
