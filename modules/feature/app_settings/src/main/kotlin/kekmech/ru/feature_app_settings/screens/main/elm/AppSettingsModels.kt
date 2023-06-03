package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.domain_app_settings_models.AppEnvironment
import kekmech.ru.domain_app_settings_models.AppSettings
import kekmech.ru.domain_github.dto.GitHubUser

internal data class AppSettingsState(
    val appSettings: AppSettings? = null,
    val isFeatureToggleSnowFlakesEnabled: Boolean,
    val contributors: List<GitHubUser>? = null
)

internal sealed interface AppSettingsEvent {

    sealed interface Ui : AppSettingsEvent {

        object Init : Ui

        object Action {
            data class SetDarkThemeEnabled(val isEnabled: Boolean) : Ui
            data class SetSnowEnabled(val isEnabled: Boolean) : Ui
            data class SetShowQuickNavigationFab(val isVisible: Boolean) : Ui
            data class SetAutoHideBottomSheet(val isEnabled: Boolean) : Ui
            data class ChangeBackendEnvironment(val appEnvironment: AppEnvironment) : Ui
            object ClearSelectedGroup : Ui
            data class LanguageChanged(val selectedLanguage: String) : Ui
            data class MapTypeChanged(val selectedMapType: String) : Ui
        }

        object Click {
            object Language : Ui
            object MapType : Ui
        }
    }

    sealed interface Internal : AppSettingsEvent {
        data class LoadAppSettingsSuccess(val appSettings: AppSettings) : Internal
        data class ObserveContributorsSuccess(val contributors: List<GitHubUser>) : Internal
    }
}

internal sealed interface AppSettingsCommand {
    object LoadAppSettings : AppSettingsCommand

    data class SetDarkThemeEnabled(val isEnabled: Boolean) : AppSettingsCommand
    data class SetSnowEnabled(val isEnabled: Boolean) : AppSettingsCommand
    data class SetShowQuickNavigationFab(val isVisible: Boolean) : AppSettingsCommand
    data class SetAutoHideBottomSheet(val isEnabled: Boolean) : AppSettingsCommand
    data class ChangeBackendEnvironment(val appEnvironment: AppEnvironment) : AppSettingsCommand
    data class ChangeLanguage(val selectedLanguage: String) : AppSettingsCommand
    data class ChangeMapType(val selectedMapType: String) : AppSettingsCommand
    object ClearSelectedGroup : AppSettingsCommand
    object ObserveContributors : AppSettingsCommand
    object FetchContributors : AppSettingsCommand
}

internal sealed interface AppSettingsEffect {
    object RecreateActivity : AppSettingsEffect
    data class OpenLanguageSelectionDialog(val selectedLanguage: String) : AppSettingsEffect
    data class OpenMapTypeDialog(val mapType: String) : AppSettingsEffect
}
