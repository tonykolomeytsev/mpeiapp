package kekmech.ru.feature_app_settings_impl.presentation.screens.main.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEffect
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsEvent.Ui
import kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm.AppSettingsState
import kekmech.ru.res_strings.R.string as Strings
import kekmech.ru.ui_kit_lists.ListItem
import kekmech.ru.ui_kit_switch.Switch
import kekmech.ru.ui_kit_topappbar.TopAppBar
import kekmech.ru.ui_theme.theme.MpeixTheme
import kotlinx.coroutines.flow.Flow

@Composable
internal fun AppSettingsComposeScreen(
    state: AppSettingsState,
    effects: Flow<AppSettingsEffect>,
    onAccept: (AppSettingsEvent) -> Unit,
    onBackClick: (() -> Unit)? = null,
    onOpenFavorites: () -> Unit,
    onOpenGithub: () -> Unit,
    onOpenContributor: (String) -> Unit,
    onLogoutBars: () -> Unit,
    onOpenLanguageSelection: (String) -> Unit,
    onOpenMapTypeSelection: (String) -> Unit,
    onRecreateActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is AppSettingsEffect.RecreateActivity -> onRecreateActivity()
                is AppSettingsEffect.OpenLanguageSelectionDialog -> onOpenLanguageSelection(effect.selectedLanguage)
                is AppSettingsEffect.OpenMapTypeDialog -> onOpenMapTypeSelection(effect.mapType)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(Strings.app_settings_screen_title),
                navigationIcon = {
                    onBackClick?.let { BackIconButton(onClick = it) }
                },
            )
        },
        containerColor = MpeixTheme.palette.background,
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        val appSettings = state.appSettings
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (appSettings != null) {
                // Section 1: Appearance
                item {
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_dark_theme),
                        supportingText = stringResource(Strings.app_settings_dark_theme_description),
                        trailingContent = {
                            Switch(
                                checked = appSettings.isDarkThemeEnabled,
                                onCheckedChange = { isChecked ->
                                    onAccept(Ui.Action.SetDarkThemeEnabled(isChecked))
                                },
                            )
                        },
                    )
                }

                item {
                    val langLabel = when (appSettings.languageCode) {
                        "ru_RU" -> "РУССКИЙ"
                        "en_US" -> "ENGLISH"
                        else -> ""
                    }
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_section_lang),
                        supportingText = stringResource(Strings.app_settings_section_lang_description),
                        trailingContent = {
                            SupportingText(text = langLabel)
                        },
                        modifier = Modifier.clickable {
                            onAccept(Ui.Click.Language)
                        },
                    )
                }

                if (state.isFeatureToggleSnowFlakesEnabled) {
                    item {
                        ListItem(
                            headlineText = stringResource(Strings.app_settings_snowy_mood),
                            trailingContent = {
                                Switch(
                                    checked = appSettings.isSnowEnabled,
                                    onCheckedChange = { isChecked ->
                                        onAccept(Ui.Action.SetSnowEnabled(isChecked))
                                    },
                                )
                            },
                        )
                    }
                }

                // Section 2: Schedule
                item {
                    SettingsSectionHeader(title = stringResource(Strings.app_settings_header_schedule))
                }
                item {
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_favorite_schedule_title),
                        supportingText = stringResource(Strings.app_settings_favorite_schedule_description),
                        modifier = Modifier.clickable {
                            onOpenFavorites()
                        },
                    )
                }
                item {
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_show_navigation_fab),
                        supportingText = stringResource(Strings.app_settings_show_navigation_fab_description),
                        trailingContent = {
                            Switch(
                                checked = appSettings.showNavigationButton,
                                onCheckedChange = { isChecked ->
                                    onAccept(Ui.Action.SetShowQuickNavigationFab(isChecked))
                                },
                            )
                        },
                    )
                }

                // Section 3: Map
                item {
                    SettingsSectionHeader(title = stringResource(Strings.app_settings_header_map))
                }
                item {
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_auto_hide_bottom_sheet),
                        supportingText = stringResource(Strings.app_settings_auto_hide_bottom_sheet_description),
                        trailingContent = {
                            Switch(
                                checked = appSettings.autoHideBottomSheet,
                                onCheckedChange = { isChecked ->
                                    onAccept(Ui.Action.SetAutoHideBottomSheet(isChecked))
                                },
                            )
                        },
                    )
                }
                item {
                    val mapTypeLabel = when (appSettings.mapAppearanceType) {
                        "hybrid" -> stringResource(Strings.change_map_type_hybrid).uppercase()
                        "scheme" -> stringResource(Strings.change_map_type_scheme).uppercase()
                        else -> ""
                    }
                    ListItem(
                        headlineText = stringResource(Strings.app_settings_map_appearance_type),
                        trailingContent = {
                            SupportingText(text = mapTypeLabel)
                        },
                        modifier = Modifier.clickable {
                            onAccept(Ui.Click.MapType)
                        },
                    )
                }

                // Section 4: BARS
                item {
                    SettingsSectionHeader(title = stringResource(Strings.app_settings_header_bars))
                }
                item {
                    ListItem(
                        headlineText = "Выйти из аккаунта",
                        supportingText = "Нажмите чтобы завершить сессию в БАРС",
                        modifier = Modifier.clickable {
                            onLogoutBars()
                        },
                    )
                }

                // Section 5: Support & Fork
                item {
                    SettingsSectionHeader(title = stringResource(Strings.app_settings_header_support))
                }
                item {
                    ListItem(
                        headlineText = "Репозиторий MpeiX Next",
                        supportingText = "github.com/ghostoftheeleven/mpei-next",
                        modifier = Modifier.clickable {
                            onOpenGithub()
                        },
                    )
                }
                item {
                    ListItem(
                        headlineText = "Оригинальный проект (MpeiX)",
                        supportingText = "Форк от github.com/tonykolomeytsev/mpeiapp",
                        modifier = Modifier.clickable {
                            onOpenContributor("https://github.com/tonykolomeytsev/mpeiapp")
                        },
                    )
                }

                // Section 6: Contributors
                val contributorsList = state.contributors?.takeIf { it.isNotEmpty() } ?: listOf(
                    kekmech.ru.feature_contributors_api.domain.model.Contributor(
                        login = "ghostoftheeleven",
                        name = "Расул Рустамов",
                        bio = "Разработчик MpeiX Next • студент МЭИ",
                        url = "https://github.com/ghostoftheeleven",
                        avatarUrl = "https://avatars.githubusercontent.com/u/98048622?v=4",
                    )
                )
                item {
                    SettingsSectionHeader(title = stringResource(Strings.app_settings_header_contributors))
                }
                items(contributorsList) { contributor ->
                    val displayName = contributor.name ?: contributor.login
                    ListItem(
                        headlineText = displayName,
                        supportingText = contributor.bio,
                        leadingContent = {
                            Monogram(letter = displayName.firstOrNull() ?: 'C')
                        },
                        modifier = Modifier.clickable {
                            onOpenContributor(contributor.url)
                        },
                    )
                }

                // Version
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = stringResource(
                            Strings.app_settings_app_version,
                            state.appVersionName.versionName,
                        ),
                        style = MpeixTheme.typography.paragraphNormal,
                        color = MpeixTheme.palette.contentVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MpeixTheme.typography.header4,
        color = MpeixTheme.palette.content,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
    )
}
