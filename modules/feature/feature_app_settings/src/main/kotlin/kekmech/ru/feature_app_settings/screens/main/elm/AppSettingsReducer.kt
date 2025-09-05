package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Internal
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Ui
import money.vivid.elmslie.core.store.ScreenReducer
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsCommand as Command
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEffect as Effect
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent as Event
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsState as State

internal class AppSettingsReducer :
    ScreenReducer<Event, Ui, Internal, State, Effect, Command>(
        uiEventClass = Ui::class,
        internalEventClass = Internal::class,
    ) {

    override fun Result.internal(event: Internal) =
        when (event) {
            is Internal.LoadAppSettingsSuccess -> state { copy(appSettings = event.appSettings) }
            is Internal.ObserveContributorsSuccess -> state { copy(contributors = event.contributors) }
        }

    override fun Result.ui(event: Ui) =
        when (event) {
            is Ui.Init -> commands {
                +Command.LoadAppSettings
                +Command.ObserveContributors
            }
            is Ui.Action.SetDarkThemeEnabled -> {
                effects {
                    +Effect.RecreateActivity.takeIf { event.isEnabled != state.appSettings?.isDarkThemeEnabled }
                }
                commands { +Command.SetDarkThemeEnabled(event.isEnabled) }
            }
            is Ui.Action.SetSnowEnabled -> commands { +Command.SetSnowEnabled(event.isEnabled) }
            is Ui.Action.SetAutoHideBottomSheet -> commands {
                +Command.SetAutoHideBottomSheet(event.isEnabled)
            }
            is Ui.Action.ClearSelectedGroup -> commands {
                +Command.ClearSelectedGroup
            }
            is Ui.Action.ChangeBackendEnvironment -> commands {
                +Command.ChangeBackendEnvironment(event.appEnvironment)
            }
            is Ui.Click.Language -> effects {
                +state.appSettings?.let { Effect.OpenLanguageSelectionDialog(it.languageCode) }
            }
            is Ui.Click.MapType -> effects {
                +state.appSettings?.let { Effect.OpenMapTypeDialog(it.mapAppearanceType) }
            }
            is Ui.Action.LanguageChanged -> {
                effects { +Effect.RecreateActivity }
                commands { +Command.ChangeLanguage(event.selectedLanguage) }
            }
            is Ui.Action.MapTypeChanged -> {
                effects { +Effect.RecreateActivity }
                commands { +Command.ChangeMapType(event.selectedMapType) }
            }
            is Ui.Action.SetShowQuickNavigationFab -> commands {
                +Command.SetShowQuickNavigationFab(event.isVisible)
            }
        }
}
