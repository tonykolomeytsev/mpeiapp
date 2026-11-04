package kekmech.ru.ui_kit_lists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kekmech.ru.ui_theme.theme.MpeixTheme

private val ActionShape = RoundedCornerShape(8.dp)

/**
 * # SectionHeader
 *
 * Title of the group of [ListItem]s, or title of the bottom sheet with list.
 *
 * Usage example:
 * ```kotlin
 * SectionHeader(title = "Language")
 * ListItem(headlineText = "Русский")
 * ListItem(headlineText = "English")
 * ```
 *
 * @param title the title of the section
 * @param modifier [Modifier] to be applied to the section header
 * @param subtitle the optional text displayed below the title
 * @param actionText the optional text of the action at the end of the header
 * @param onActionClick called when the action is clicked, the action is shown only with [actionText]
 */
@Composable
public fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MpeixTheme.typography.titleLarge,
                color = MpeixTheme.palette.onSurface,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MpeixTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MpeixTheme.palette.onSurfaceVariant,
                )
            }
        }
        if (actionText != null) {
            Text(
                text = actionText.uppercase(),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(ActionShape)
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                style = MpeixTheme.typography.bodyMedium,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MpeixTheme.palette.primary,
            )
        }
    }
}

@Preview
@Composable
private fun SectionHeaderPreview() {
    MpeixTheme {
        Column(
            modifier = Modifier.background(MpeixTheme.palette.surface)
        ) {
            SectionHeader(title = "Section title")
            SectionHeader(title = "Section title", subtitle = "Section subtitle")
            SectionHeader(title = "Section title", actionText = "Show all")
            SectionHeader(title = "Section title", subtitle = "Section subtitle", actionText = "Set up")
        }
    }
}
