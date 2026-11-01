package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_switch.embedded.EmbeddedSwitch
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Whole row toggles the setting, so the switch itself is not clickable.
 * Optional [description] is shown below the row and is not clickable.
 */
@Composable
internal fun ToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String? = null,
) {
    ListItem(
        headlineText = title,
        modifier = Modifier.toggleable(
            value = checked,
            role = Role.Switch,
            onValueChange = onCheckedChange,
        ),
        trailingContent = {
            EmbeddedSwitch(checked = checked)
        },
    )
    if (description != null) {
        Description(description)
    }
}

@Composable
@PreviewLightDark
private fun ToggleItemPreview() {
    MpeixPreview {
        Column(Modifier.background(MpeixTheme.palette.surface)) {
            ToggleItem(
                title = "Dark theme",
                checked = true,
                onCheckedChange = { /* no-op */ },
                description = "You can change the visual design from light to dark and vice versa",
            )
            Spacer(Modifier.height(16.dp))
            ToggleItem(
                title = "Snowy mood",
                checked = false,
                onCheckedChange = { /* no-op */ },
            )
        }
    }
}
