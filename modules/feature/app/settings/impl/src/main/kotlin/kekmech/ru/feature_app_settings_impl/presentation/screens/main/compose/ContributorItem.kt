package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.theme.MpeixTheme

private const val BioMaxLines = 3

// colorGray30 of the old XML theme, theme palette has no such color
private val AvatarPlaceholderLight = Color(0xFFD0D0D0)
private val AvatarPlaceholderDark = Color(0xFF3C3C3C)

/**
 * GitHub contributor: round avatar, name, login and bio.
 * Name and bio are hidden when the contributor has not filled them in.
 */
@Composable
internal fun ContributorItem(
    contributor: Contributor,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Avatar(url = contributor.avatarUrl)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            contributor.name?.let { name ->
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = 2.dp),
                    style = MpeixTheme.typography.titleSmall,
                    color = MpeixTheme.palette.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = contributor.login,
                modifier = Modifier.padding(bottom = 4.dp),
                style = MpeixTheme.typography.bodyMedium,
                color = MpeixTheme.palette.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            contributor.bio?.let { bio ->
                Text(
                    text = bio,
                    style = MpeixTheme.typography.bodyMedium,
                    color = MpeixTheme.palette.onSurface,
                    maxLines = BioMaxLines,
                    overflow = TextOverflow.Clip,
                )
            }
        }
    }
}

/**
 * Placeholder circle stays under the image, so it is visible while loading and on error.
 */
@Composable
private fun Avatar(url: String) {
    val isDarkTheme = MpeixTheme.palette.surface.luminance() < 0.5f
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isDarkTheme) AvatarPlaceholderDark else AvatarPlaceholderLight),
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
@PreviewLightDark
private fun ContributorItemPreview() {
    MpeixPreview {
        Column(Modifier.background(MpeixTheme.palette.surface)) {
            ContributorItem(
                contributor = Contributor(
                    login = "tonykolomeytsev",
                    name = "Anton Kolomeytsev",
                    bio = "Android Developer",
                    url = "",
                    avatarUrl = "",
                ),
                onClick = { /* no-op */ },
            )
            ContributorItem(
                contributor = Contributor(
                    login = "gh_login",
                    name = "Some some name",
                    bio = null,
                    url = "",
                    avatarUrl = "",
                ),
                onClick = { /* no-op */ },
            )
        }
    }
}
