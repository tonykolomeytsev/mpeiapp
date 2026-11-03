package kekmech.ru.feature_onboarding_impl.presentation.screen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.res_strings.R.string as Strings

@Composable
internal fun SettingsBlock(
    darkThemeEnabled: Boolean,
    languageCode: String,
    onToggleDarkTheme: (Boolean) -> Unit,
    onClickLanguage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MpeixTheme.palette.surfaceContainer),
    ) {
        ListItem(
            headlineText = stringResource(Strings.app_settings_dark_theme),
            modifier = Modifier.toggleable(
                value = darkThemeEnabled,
                role = Role.Switch,
                onValueChange = onToggleDarkTheme,
            ),
            trailingContent = {
                Switch(checked = darkThemeEnabled)
            },
        )
        ListItem(
            headlineText = stringResource(Strings.app_settings_section_lang),
            modifier = Modifier.clickable(
                role = Role.Button,
                onClick = onClickLanguage,
            ),
            trailingContent = {
                SupportingText(languageName(languageCode))
            },
        )
    }
}

// Language names are shown in their own language, so they are not localized
private fun languageName(languageCode: String): String =
    when (languageCode) {
        "en_US" -> "ENGLISH"
        else -> "РУССКИЙ"
    }

@Composable
@PreviewLightDark
private fun SettingsBlockPreview() {
    MpeixTheme {
        Box(
            Modifier
                .background(MpeixTheme.palette.surface)
                .padding(16.dp)
        ) {
            SettingsBlock(
                darkThemeEnabled = isSystemInDarkTheme(),
                languageCode = "ru_RU",
                onToggleDarkTheme = { /* no-op */ },
                onClickLanguage = { /* no-op */ },
            )
        }
    }
}
