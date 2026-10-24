package kekmech.ru.ui_kit_segmented

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

/**
 * Row of equally wide options, exactly one of them is selected.
 * Colors come from [MpeixTheme], so it follows light / dark / system themes.
 *
 * @param role accessibility role of the options: [Role.Tab] for tabs, [Role.RadioButton] for a setting
 */
@Composable
public fun <T> SegmentedControl(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    role: Role = Role.Tab,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            val shape = RoundedCornerShape(8.dp)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(shape)
                    .background(
                        color = if (isSelected) MpeixTheme.palette.primaryContainer else MpeixTheme.palette.surface,
                        shape = shape,
                    )
                    .selectable(selected = isSelected, role = role, onClick = { onSelect(option) })
                    .padding(vertical = 10.dp, horizontal = 4.dp),
            ) {
                Text(
                    text = label(option),
                    style = if (isSelected) {
                        MpeixTheme.typography.paragraphNormalAccent
                    } else {
                        MpeixTheme.typography.paragraphNormal
                    },
                    color = if (isSelected) MpeixTheme.palette.content else MpeixTheme.palette.contentVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SegmentedControlPreview() {
    MpeixTheme {
        SegmentedControl(
            options = listOf("Светлая", "Тёмная", "Системная"),
            selected = "Тёмная",
            onSelect = {},
            label = { it },
            modifier = Modifier.padding(16.dp),
        )
    }
}
