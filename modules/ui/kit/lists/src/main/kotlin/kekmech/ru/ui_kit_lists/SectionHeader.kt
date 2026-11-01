package kekmech.ru.ui_kit_lists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme

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
 */
@Composable
public fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MpeixTheme.typography.titleLarge,
            color = MpeixTheme.palette.onSurface,
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                modifier = Modifier.padding(top = 2.dp),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
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
        }
    }
}
