package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_theme.theme.MpeixTheme

/** A person of the team: exactly the people from [Developers] are shown in the settings. */
internal data class Developer(
    @StringRes val nameRes: Int,
    @StringRes val roleRes: Int,
    val url: String,
)

internal val Developers = listOf(
    Developer(
        nameRes = Strings.developer_rasul_rustamov_name,
        roleRes = Strings.contributor_rasul_rustamov_bio,
        url = "https://github.com/ghostoftheeleven",
    ),
    Developer(
        nameRes = Strings.developer_ivan_myalitsin_name,
        roleRes = Strings.developer_ivan_myalitsin_role,
        url = "https://github.com/bezpaniki-arch",
    ),
    Developer(
        nameRes = Strings.developer_johnny_wickky_name,
        roleRes = Strings.contributor_johnny_wickky_bio,
        url = "https://github.com/Johnny-Wickky",
    ),
)

/** "Developers" section: three cards of the same size and the same look. */
@Composable
internal fun DevelopersSection(
    onOpenProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        SettingsSectionHeader(title = stringResource(Strings.app_settings_header_developers))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Developers.forEach { developer ->
                DeveloperCard(developer = developer, onOpenProfile = onOpenProfile)
            }
        }
    }
}

@Composable
private fun DeveloperCard(
    developer: Developer,
    onOpenProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = stringResource(developer.nameRes)
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MpeixTheme.palette.surface,
        border = BorderStroke(1.dp, MpeixTheme.palette.outline),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onOpenProfile(developer.url) },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            Surface(
                shape = CircleShape,
                color = MpeixTheme.palette.primaryContainer,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = name.first().uppercase(),
                        style = MpeixTheme.typography.header4,
                        color = MpeixTheme.palette.contentAccent,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = name,
                    style = MpeixTheme.typography.paragraphBig.copy(fontWeight = FontWeight.Medium),
                    color = MpeixTheme.palette.content,
                )
                Text(
                    text = stringResource(developer.roleRes),
                    style = MpeixTheme.typography.paragraphNormal,
                    color = MpeixTheme.palette.contentVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
    }
}
