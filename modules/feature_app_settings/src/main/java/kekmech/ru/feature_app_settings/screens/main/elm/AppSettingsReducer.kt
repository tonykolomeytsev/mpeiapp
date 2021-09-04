package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.News
import kekmech.ru.feature_app_settings.screens.main.elm.AppSettingsEvent.Wish
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer
import java.util.*

internal class AppSettingsReducer :
    StateReducer<AppSettingsEvent, AppSettingsState, AppSettingsEffect, AppSettingsAction> {

    override fun reduce(
        event: AppSettingsEvent,
        state: AppSettingsState
    ): Result<AppSettingsState, AppSettingsEffect, AppSettingsAction> = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: AppSettingsState
    ): Result<AppSettingsState, AppSettingsEffect, AppSettingsAction> = when (event) {
        is News.AppSettingsLoaded -> Result(
            state = state.copy(
                appSettings = event.appSettings,
                hash = UUID.randomUUID().toString()
            )
        )
        is News.AppSettingsChanged -> Result(state = state.copy(hash = UUID.randomUUID().toString()))
    }

    private fun reduceWish(
        event: Wish,
        state: AppSettingsState
    ): Result<AppSettingsState, AppSettingsEffect, AppSettingsAction> = when (event) {
        is Wish.Init -> Result(
            state = state,
            command = AppSettingsAction.LoadAppSettings
        )
        is Wish.Action.SetDarkThemeEnabled -> Result(
            state = state,
            command = AppSettingsAction.SetDarkThemeEnabled(event.isEnabled),
            effect = AppSettingsEffect.RecreateActivity
                .takeIf { event.isEnabled != state.appSettings?.isDarkThemeEnabled }
        )
        is Wish.Action.SetSnowEnabled -> Result(
            state = state,
            command = AppSettingsAction.SetSnowEnabled(event.isEnabled)
        )
        is Wish.Action.SetAutoHideBottomSheet -> Result(
            state = state,
            command = AppSettingsAction.SetAutoHideBottomSheet(event.isEnabled)
        )
        is Wish.Action.ClearSelectedGroup -> Result(
            state = state,
            command = AppSettingsAction.ClearSelectedGroup
        )
        is Wish.Action.ChangeBackendEnvironment -> Result(
            state = state,
            command = AppSettingsAction.ChangeBackendEnvironment(event.isDebug)
        )
        is Wish.Click.OnLanguage -> Result(
            state = state,
            effect = state.appSettings?.let { AppSettingsEffect.OpenLanguageSelectionDialog(it.languageCode) }
        )
        is Wish.Click.MapType -> Result(
            state = state,
            effect = state.appSettings?.let { AppSettingsEffect.OpenMapTypeDialog(it.mapAppearanceType) }
        )
        is Wish.Action.LanguageChanged -> Result(
            state = state,
            effect = AppSettingsEffect.RecreateActivity,
            command = AppSettingsAction.ChangeLanguage(event.selectedLanguage)
        )
        is Wish.Action.MapTypeChanged -> Result(
            state = state,
            effect = AppSettingsEffect.RecreateActivity,
            command = AppSettingsAction.ChangeMapType(event.selectedMapType)
        )
        is Wish.Action.SetShowQuickNavigationFab -> Result(
            state = state,
            command = AppSettingsAction.SetShowQuickNavigationFab(event.isVisible)
        )
    }

}