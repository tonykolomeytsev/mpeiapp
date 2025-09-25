package kekmech.ru.ui_kit_lists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ListItemDefaults
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
                style = MpeixTheme.typography.paragraphBig,
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
                        style = MpeixTheme.typography.labelMini,
                    )
                }
            }
        },
        supportingContent = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MpeixTheme.typography.paragraphNormal,
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
            headlineColor = MpeixTheme.palette.content,
            leadingIconColor = MpeixTheme.palette.contentVariant,
            overlineColor = MpeixTheme.palette.contentVariant,
            supportingColor = MpeixTheme.palette.contentVariant,
            trailingIconColor = MpeixTheme.palette.contentVariant,
            disabledHeadlineColor = MpeixTheme.palette.contentDisabled,
            disabledLeadingIconColor = MpeixTheme.palette.contentDisabled,
            disabledTrailingIconColor = MpeixTheme.palette.contentDisabled,
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
            tint = MpeixTheme.palette.contentVariant,
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
            tint = MpeixTheme.palette.contentVariant,
        )
    }

    /**
     * Leading monogram (Avatar placeholder with letter) for [ListItem]
     */
    @Composable
    public fun Monogram(letter: Char) {
        val backgroundColor = MpeixTheme.palette.primaryContainer
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
                style = MpeixTheme.typography.paragraphBig.copy(fontWeight = FontWeight.Medium),
                color = MpeixTheme.palette.contentVariant,
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
            style = MpeixTheme.typography.labelMini,
            color = MpeixTheme.palette.contentVariant,
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
