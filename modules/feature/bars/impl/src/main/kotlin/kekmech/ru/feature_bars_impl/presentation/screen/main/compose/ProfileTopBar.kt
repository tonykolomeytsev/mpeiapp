package kekmech.ru.feature_bars_impl.presentation.screen.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_shimmer.shimmer
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

private val AvatarSize = 40.dp
private val ContentPadding = 4.dp

/**
 * Top bar of the BARS screen: the profile pill on the left and settings on the right.
 * It is the first item of the list, so it scrolls away with the content.
 */
@Composable
internal fun ProfileTopBar(
    name: String?,
    group: String?,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .adaptiveContentWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        val displayName = name ?: stringResource(Strings.bars_stub_student_name)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f, fill = false)
                .floating(RoundedCornerShape(percent = 50))
                .clickable(enabled = !isLoading, onClick = onProfileClick)
                .padding(
                    start = ContentPadding,
                    end = 20.dp,
                    top = ContentPadding,
                    bottom = ContentPadding
                ),
        ) {
            Avatar(initials = if (isLoading) "" else displayName.initials())
            if (isLoading) {
                Box(
                    Modifier
                        .shimmer()
                        .width(120.dp)
                        .height(20.dp)
                        .background(MpeixTheme.palette.outlineVariant, RoundedCornerShape(8.dp)),
                )
            } else {
                Column {
                    Text(
                        text = displayName,
                        style = MpeixTheme.typography.titleLarge,
                        color = MpeixTheme.palette.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (!group.isNullOrEmpty()) {
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = group,
                            style = MpeixTheme.typography.labelMedium,
                            color = MpeixTheme.palette.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 12.dp)
                .size(AvatarSize + ContentPadding * 2)
                .floating(CircleShape)
                .clickable(onClick = onSettingsClick),
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = null,
                tint = MpeixTheme.palette.onSurface,
            )
        }
    }
}

/** Circle with the initials, there are no photos in BARS. */
@Composable
private fun Avatar(initials: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(AvatarSize)
            .background(MpeixTheme.palette.surfaceContainerHighest, CircleShape),
    ) {
        Text(
            text = initials,
            style = MpeixTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MpeixTheme.palette.onSurfaceVariant,
        )
    }
}

/** Same container as the floating navigation bar at the bottom. */
@Composable
private fun Modifier.floating(shape: Shape): Modifier =
    this.clip(shape)

/** "Иван Иванов" -> "ИИ" */
private fun String.initials(): String =
    split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

@PreviewLightDark
@Composable
private fun ProfileTopBarPreviewFilled() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            ProfileTopBar(
                name = "Антон Коломейцев",
                group = "С-12-16",
                onProfileClick = { /* no-op */ },
                onSettingsClick = { /* no-op */ },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProfileTopBarPreviewEmpty() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            ProfileTopBar(
                name = null,
                group = null,
                onProfileClick = { /* no-op */ },
                onSettingsClick = { /* no-op */ },
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ProfileTopBarPreviewShimmered() {
    MpeixTheme {
        Box(Modifier.background(MpeixTheme.palette.surface)) {
            ProfileTopBar(
                name = null,
                group = null,
                isLoading = true,
                onProfileClick = { /* no-op */ },
                onSettingsClick = { /* no-op */ },
            )
        }
    }
}
