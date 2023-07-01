package kekmech.ru.ui_kit_switch

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * # Switch
 *
 * Mapping implementation to [androidx.compose.material3.Switch].
 * Switches toggle the state of a single item on or off.
 *
 * @param checked whether or not this switch is checked
 * @param onCheckedChange called when this switch is clicked. If `null`, then this switch will not
 * be interactable, unless something else handles its input events and updates its state.
 * @param modifier the [Modifier] to be applied to this switch
 * @param enabled controls the enabled state of this switch. When `false`, this component will not
 * respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this switch. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this switch in different states.
 */
@Composable
@NonRestartableComposable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    androidx.compose.material3.Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        thumbContent = null,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MpeixTheme.palette.contentAccent,
            checkedTrackColor = MpeixTheme.palette.primary,
            checkedBorderColor = MpeixTheme.palette.primary,
            checkedIconColor = MpeixTheme.palette.primary,
            uncheckedThumbColor = MpeixTheme.palette.outline,
            uncheckedTrackColor = MpeixTheme.palette.surfacePlus1,
            uncheckedBorderColor = MpeixTheme.palette.outline,
            uncheckedIconColor = MpeixTheme.palette.surfacePlus1,
            disabledCheckedThumbColor = MpeixTheme.palette.surface,
            disabledCheckedTrackColor = MpeixTheme.palette.content.copy(alpha = 0.20f),
            disabledCheckedBorderColor = Color.Transparent,
            disabledCheckedIconColor = MpeixTheme.palette.content.copy(alpha = 0.30f),
            disabledUncheckedThumbColor = MpeixTheme.palette.content.copy(alpha = 0.38f),
            disabledUncheckedTrackColor = MpeixTheme.palette.surfacePlus1,
            disabledUncheckedBorderColor = MpeixTheme.palette.content.copy(alpha = 0.10f),
            disabledUncheckedIconColor = MpeixTheme.palette.surface.copy(alpha = 0.30f),
        ),
        interactionSource = interactionSource,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun SwitchPreviewLight() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.background)
                .padding(16.dp),
        ) {
            Row {
                Switch(
                    checked = false,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                )
                Switch(
                    checked = true,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                )
                Switch(
                    checked = false,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                    enabled = false,
                )
                Switch(
                    checked = true,
                    onCheckedChange = { /* no-op */ },
                    enabled = false,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun SwitchPreviewDark() {
    MpeixTheme {
        Column(
            modifier = Modifier
                .background(MpeixTheme.palette.background)
                .padding(16.dp),
        ) {
            Row {
                Switch(
                    checked = false,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                )
                Switch(
                    checked = true,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                )
                Switch(
                    checked = false,
                    onCheckedChange = { /* no-op */ },
                    modifier = Modifier.padding(end = 16.dp),
                    enabled = false,
                )
                Switch(
                    checked = true,
                    onCheckedChange = { /* no-op */ },
                    enabled = false,
                )
            }
        }
    }
}
