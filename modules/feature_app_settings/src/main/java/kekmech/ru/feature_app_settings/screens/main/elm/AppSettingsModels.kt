package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.domain_app_settings.AppSettings

internal data class AppSettingsState(
    val appSettings: AppSettings? = null,
    val isFeatureToggleSnowFlakesEnabled: Boolean,
    val hash: String = ""
)

internal sealed class AppSettingsEvent {

    sealed class Wish : AppSettingsEvent() {

        object Init : Wish()

        object Action {
            data class SetDarkThemeEnabled(val isEnabled: Boolean) : Wish()
            data class SetSnowEnabled(val isEnabled: Boolean) : Wish()
            data class SetShowQuickNavigationFab(val isVisible: Boolean) : Wish()
            data class SetChangeDayAfterChangeWeek(val isEnabled: Boolean) : Wish()
            data class SetAutoHideBottomSheet(val isEnabled: Boolean) : Wish()
            data class ChangeBackendEnvironment(val isDebug: Boolean) : Wish()
            object ClearSelectedGroup : Wish()
            data class LanguageChanged(val selectedLanguage: String) : Wish()
        }

        object Click {
            object OnLanguage : Wish()
        }
    }

    sealed class News : AppSettingsEvent() {
        data class AppSettingsLoaded(val appSettings: AppSettings) : News()
        object AppSettingsChanged : News()
    }
}

internal sealed class AppSettingsAction {
    object LoadAppSettings : AppSettingsAction()

    data class SetDarkThemeEnabled(val isEnabled: Boolean) : AppSettingsAction()
    data class SetSnowEnabled(val isEnabled: Boolean) : AppSettingsAction()
    data class SetShowQuickNavigationFab(val isVisible: Boolean) : AppSettingsAction()
    data class SetChangeDayAfterChangeWeek(val isEnabled: Boolean) : AppSettingsAction()
    data class SetAutoHideBottomSheet(val isEnabled: Boolean) : AppSettingsAction()
    data class ChangeBackendEnvironment(val isDebug: Boolean) : AppSettingsAction()
    data class ChangeLanguage(val selectedLanguage: String) : AppSettingsAction()
    object ClearSelectedGroup : AppSettingsAction()
}

internal sealed class AppSettingsEffect {
    object RecreateActivity : AppSettingsEffect()
    data class OpenLanguageSelectionDialog(val selectedLanguage: String) : AppSettingsEffect()
}