package kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm

import kekmech.ru.feature_app_settings_api.domain.AppSettings
import kekmech.ru.feature_contributors_api.domain.model.Contributor
import kekmech.ru.library_app_info.AppVersionName

internal data class AppSettingsState(
    val appSettings: AppSettings? = null,
    val isFeatureToggleSnowFlakesEnabled: Boolean,
    val contributors: List<Contributor>? = null,
    val appVersionName: AppVersionName,
)

internal sealed interface AppSettingsEvent {

    sealed interface Ui : AppSettingsEvent {

        object Init : Ui

        object Action {
            data class SetDarkThemeEnabled(val isEnabled: Boolean) : Ui
            data class SetSnowEnabled(val isEnabled: Boolean) : Ui
            data class SetShowQuickNavigationFab(val isVisible: Boolean) : Ui
            data class SetAutoHideBottomSheet(val isEnabled: Boolean) : Ui
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
        data class ObserveContributorsSuccess(val contributors: List<Contributor>) : Internal
    }
}

internal sealed interface AppSettingsCommand {
    object LoadAppSettings : AppSettingsCommand

    data class SetDarkThemeEnabled(val isEnabled: Boolean) : AppSettingsCommand
    data class SetSnowEnabled(val isEnabled: Boolean) : AppSettingsCommand
    data class SetShowQuickNavigationFab(val isVisible: Boolean) : AppSettingsCommand
    data class SetAutoHideBottomSheet(val isEnabled: Boolean) : AppSettingsCommand
    data class ChangeLanguage(val selectedLanguage: String) : AppSettingsCommand
    data class ChangeMapType(val selectedMapType: String) : AppSettingsCommand
    object ObserveContributors : AppSettingsCommand
    object FetchContributors : AppSettingsCommand
}

internal sealed interface AppSettingsEffect {
    object RecreateActivity : AppSettingsEffect
    data class OpenLanguageSelectionDialog(val selectedLanguage: String) : AppSettingsEffect
    data class OpenMapTypeDialog(val mapType: String) : AppSettingsEffect
}
