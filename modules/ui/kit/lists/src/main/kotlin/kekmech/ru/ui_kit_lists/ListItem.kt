package kekmech.ru.ui_kit_lists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_theme.theme.MpeixTheme


// Material 3 opacity for disabled content
private const val DisabledContentAlpha = 0.38f

/**
 * # ListItem
 *
 * Mapping implementation to [androidx.compose.material3.ListItem].
 * Lists are continuous, vertical indexes of text or images.
 *
 * Usage example:
 * ```kotlin
 * ListItem(
 *     headlineText = "Profile",
 *     leadingContent = {
 *         Icon(MpeixIcons.Person24)
 *     },
 * )
 * ```
 *
 * @param headlineText the headline content of the list item
 * @param modifier [Modifier] to be applied to the list item
 * @param overlineText the content displayed above the headline content
 * @param supportingText the supporting content of the list item
 * @param leadingContent the leading content:
 *   - [ListItemLeadingContentScope.Icon]
 *   - [ListItemLeadingContentScope.Monogram]
 * @param trailingContent the trailing content:
 *   - [ListItemTrailingContentScope.SupportingText]
 *
 * @see androidx.compose.material3.ListItem
 */
@Composable
public fun ListItem(
    headlineText: String,
    modifier: Modifier = Modifier,
    overlineText: String? = null,
    supportingText: String? = null,
    leadingContent: @Composable (ListItemLeadingContentScope.() -> Unit)? = null,
    trailingContent: @Composable (ListItemTrailingContentScope.() -> Unit)? = null,
) {
    androidx.compose.material3.ListItem(
        headlineContent = {
            Text(
                text = headlineText,
                style = MpeixTheme.typography.bodyLarge,
            )
        },
        modifier = modifier,
        overlineContent = when {
            overlineText == null -> null
            overlineText.isEmpty() -> {
                {}
            }

            else -> {
                {
                    Text(
                        text = overlineText,
                        style = MpeixTheme.typography.labelSmall,
                    )
                }
            }
        },
        supportingContent = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MpeixTheme.typography.bodyMedium,
                )
            }
        },
        leadingContent = if (leadingContent != null) {
            { leadingContent.invoke(ListItemLeadingContentScope) }
        } else {
            null
        },
        trailingContent = if (trailingContent != null) {
            { trailingContent.invoke(ListItemTrailingContentScope) }
        } else {
            null
        },
        colors = ListItemDefaults.colors(
            containerColor = MpeixTheme.palette.surface,
            headlineColor = MpeixTheme.palette.onSurface,
            leadingIconColor = MpeixTheme.palette.onSurfaceVariant,
            overlineColor = MpeixTheme.palette.onSurfaceVariant,
            supportingColor = MpeixTheme.palette.onSurfaceVariant,
            trailingIconColor = MpeixTheme.palette.onSurfaceVariant,
            disabledHeadlineColor = MpeixTheme.palette.onSurface.copy(alpha = DisabledContentAlpha),
            disabledLeadingIconColor = MpeixTheme.palette.onSurface.copy(alpha = DisabledContentAlpha),
            disabledTrailingIconColor = MpeixTheme.palette.onSurface.copy(alpha = DisabledContentAlpha),
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    )
}

public object ListItemLeadingContentScope {

    /**
     * Leading icon for [ListItem]
     */
    @Composable
    public fun Icon(painter: Painter) {
        androidx.compose.material3.Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = LocalContentColor.current,
        )
    }

    /**
     * Leading icon for [ListItem]
     */
    @Composable
    public fun Icon(imageVector: ImageVector) {
        androidx.compose.material3.Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = LocalContentColor.current,
        )
    }

    /**
     * Leading monogram (Avatar placeholder with letter) for [ListItem]
     */
    @Composable
    public fun Monogram(letter: Char) {
        val backgroundColor = MpeixTheme.palette.surfaceContainer
        Box(
            modifier = Modifier
                .size(40.dp)
                .drawBehind {
                    drawCircle(color = backgroundColor)
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "$letter",
                style = MpeixTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MpeixTheme.palette.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

public object ListItemTrailingContentScope {

    /**
     * Small trailing text for [ListItem]
     */
    @Composable
    public fun SupportingText(text: String) {
        Text(
            text = text,
            style = MpeixTheme.typography.labelSmall,
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun ListItemPreview() {
    MpeixTheme {
        Column {
            ListItem(
                headlineText = "Headline",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "Overline Text",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                supportingText = "Supporting Text",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "Overline Text",
                supportingText = "Supporting Text",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "",
                supportingText = "Supporting Text\nWith second line",
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun ListItemWithLeadingIconPreview() {
    MpeixTheme {
        Column {
            ListItem(
                headlineText = "Headline",
                leadingContent = {
                    Icon(Icons.Outlined.DateRange)
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "Overline Text",
                supportingText = "Supporting Text",
                leadingContent = {
                    Icon(Icons.Outlined.ShoppingCart)
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun ListItemWithLeadingMonogramPreview() {
    MpeixTheme {
        Column {
            ListItem(
                headlineText = "Headline",
                leadingContent = {
                    Monogram(letter = 'A')
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "Overline Text",
                supportingText = "Supporting Text",
                leadingContent = {
                    Monogram(letter = 'A')
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Suppress("StringLiteralDuplication")
@Preview
@Composable
private fun ListItemWithTrailingTextPreview() {
    MpeixTheme {
        Column {
            ListItem(
                headlineText = "Headline",
                leadingContent = {
                    Monogram(letter = 'A')
                },
                trailingContent = {
                    SupportingText(text = "100+")
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
            ListItem(
                headlineText = "Headline Text",
                overlineText = "Overline Text",
                supportingText = "Supporting Text",
                leadingContent = {
                    Monogram(letter = 'A')
                },
                trailingContent = {
                    SupportingText(text = "400+")
                },
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}
