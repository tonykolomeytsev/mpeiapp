package kekmech.ru.feature_app_settings_impl.presentation.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_api.domain.model.AppSettings
import kekmech.ru.feature_app_settings_api.domain.model.AppTheme
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose.ContributorItem
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose.Description
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose.ToggleItem
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.lib_app_info.AppVersionName
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_lists.SectionHeader
import kekmech.ru.ui_kit_topappbar.topappbar.TopAppBar
import kekmech.ru.ui_preview.MpeixPreview
import kekmech.ru.ui_theme.layout.adaptiveContentWidth
import kekmech.ru.ui_theme.theme.MpeixTheme
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsState as State
import kekmech.ru.res_strings.R.string as Strings

private const val LanguageCodeRussian = "ru_RU"
private const val LanguageCodeEnglish = "en_US"
private const val MapTypeHybrid = "hybrid"
private const val MapTypeScheme = "scheme"
private const val GitHubLabel = "github.com/tonykolomeytsev/mpeiapp"

/**
 * User intentions on the settings screen.
 * Host fragment maps them to store events, navigation and analytics.
 */
internal sealed interface AppSettingsAction {
    data object BackClick : AppSettingsAction
    data class DarkThemeChange(val isEnabled: Boolean) : AppSettingsAction
    data class SnowChange(val isEnabled: Boolean) : AppSettingsAction
    data class QuickNavigationFabChange(val isVisible: Boolean) : AppSettingsAction
    data class AutoHideBottomSheetChange(val isEnabled: Boolean) : AppSettingsAction
    data object LanguageClick : AppSettingsAction
    data object MapTypeClick : AppSettingsAction
    data object FavoritesClick : AppSettingsAction
    data object BarsLogoutClick : AppSettingsAction
    data object GitHubClick : AppSettingsAction
    data class ContributorClick(val contributor: Contributor) : AppSettingsAction
}

@Composable
internal fun AppSettingsScreen(
    state: State,
    onAction: (AppSettingsAction) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackIconButton { onAction(AppSettingsAction.BackClick) }
                },
                title = stringResource(Strings.app_settings_screen_title),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .adaptiveContentWidth(),
            contentPadding = innerPadding,
        ) {
            val appSettings = state.appSettings
            if (appSettings != null) {
                settingsItems(
                    appSettings = appSettings,
                    isSnowFlakesEnabled = state.isFeatureToggleSnowFlakesEnabled,
                    onAction = onAction,
                )
            }
            if (state.contributors != null) {
                section(Strings.app_settings_header_contributors)
                items(state.contributors, key = { it.login }) { contributor ->
                    ContributorItem(
                        contributor = contributor,
                        onClick = { onAction(AppSettingsAction.ContributorClick(contributor)) },
                    )
                }
            }
            item {
                Text(
                    text = stringResource(
                        Strings.app_settings_app_version,
                        state.appVersionName.versionName,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    style = MpeixTheme.typography.titleSmall,
                    color = MpeixTheme.palette.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Suppress("LongMethod")
private fun LazyListScope.settingsItems(
    appSettings: AppSettings,
    isSnowFlakesEnabled: Boolean,
    onAction: (AppSettingsAction) -> Unit,
) {
    item {
        ToggleItem(
            title = stringResource(Strings.app_settings_dark_theme),
            description = stringResource(Strings.app_settings_dark_theme_description),
            checked = appSettings.isDarkThemeEnabled,
            onCheckedChange = { onAction(AppSettingsAction.DarkThemeChange(it)) },
        )
        Spacer(Modifier.height(16.dp))
    }
    item {
        ListItem(
            headlineText = stringResource(Strings.app_settings_section_lang),
            modifier = Modifier.clickable { onAction(AppSettingsAction.LanguageClick) },
            trailingContent = {
                SupportingText(
                    text = when (appSettings.languageCode) {
                        LanguageCodeRussian -> "РУССКИЙ"
                        LanguageCodeEnglish -> "ENGLISH"
                        else -> ""
                    },
                )
            },
        )
        Description(stringResource(Strings.app_settings_section_lang_description))
        Spacer(Modifier.height(16.dp))
    }
    if (isSnowFlakesEnabled) {
        item {
            ToggleItem(
                title = stringResource(Strings.app_settings_snowy_mood),
                checked = appSettings.isSnowEnabled,
                onCheckedChange = { onAction(AppSettingsAction.SnowChange(it)) },
            )
        }
    }

    section(Strings.app_settings_header_schedule)
    item {
        ListItem(
            headlineText = stringResource(Strings.app_settings_favorite_schedule_title),
            supportingText = stringResource(Strings.app_settings_favorite_schedule_description),
            modifier = Modifier.clickable { onAction(AppSettingsAction.FavoritesClick) },
        )
    }
    item {
        ToggleItem(
            title = stringResource(Strings.app_settings_show_navigation_fab),
            description = stringResource(Strings.app_settings_show_navigation_fab_description),
            checked = appSettings.showNavigationButton,
            onCheckedChange = { onAction(AppSettingsAction.QuickNavigationFabChange(it)) },
        )
    }

    section(Strings.app_settings_header_map)
    item {
        ToggleItem(
            title = stringResource(Strings.app_settings_auto_hide_bottom_sheet),
            description = stringResource(Strings.app_settings_auto_hide_bottom_sheet_description),
            checked = appSettings.autoHideBottomSheet,
            onCheckedChange = { onAction(AppSettingsAction.AutoHideBottomSheetChange(it)) },
        )
    }
    item {
        ListItem(
            headlineText = stringResource(Strings.app_settings_map_appearance_type),
            modifier = Modifier.clickable { onAction(AppSettingsAction.MapTypeClick) },
            trailingContent = {
                SupportingText(
                    text = when (appSettings.mapAppearanceType) {
                        MapTypeHybrid -> stringResource(Strings.change_map_type_hybrid).uppercase()
                        MapTypeScheme -> stringResource(Strings.change_map_type_scheme).uppercase()
                        else -> ""
                    },
                )
            },
        )
    }

    section(Strings.app_settings_header_bars)
    item {
        ListItem(
            headlineText = "Выйти из аккаунта",
            supportingText = "Нажмите чтобы завершить сессию в БАРС",
            modifier = Modifier.clickable { onAction(AppSettingsAction.BarsLogoutClick) },
        )
    }

    section(Strings.app_settings_header_support)
    item {
        ListItem(
            headlineText = stringResource(Strings.app_settings_section_github),
            supportingText = GitHubLabel,
            modifier = Modifier.clickable { onAction(AppSettingsAction.GitHubClick) },
        )
    }
}

private fun LazyListScope.section(titleRes: Int) {
    item(contentType = "section") {
        Spacer(Modifier.height(16.dp))
        SectionHeader(title = stringResource(titleRes))
    }
}

@Composable
@PreviewLightDark
private fun AppSettingsScreenPreview() {
    MpeixPreview {
        AppSettingsScreen(
            state = State(
                appSettings = AppSettings(
                    isDarkThemeEnabled = false,
                    appTheme = AppTheme.System,
                    isSnowEnabled = true,
                    autoHideBottomSheet = true,
                    languageCode = LanguageCodeRussian,
                    showNavigationButton = true,
                    mapAppearanceType = MapTypeHybrid,
                ),
                isFeatureToggleSnowFlakesEnabled = true,
                contributors = listOf(
                    Contributor(
                        login = "tonykolomeytsev",
                        name = "Anton Kolomeytsev",
                        bio = "Android Developer",
                        url = "",
                        avatarUrl = "",
                    ),
                ),
                appVersionName = AppVersionName("1.0.0"),
            ),
            onAction = { /* no-op */ },
        )
    }
}

@Composable
@Preview(device = Devices.TABLET)
private fun AppSettingsScreenTabletPreview() {
    AppSettingsScreenPreview()
}
