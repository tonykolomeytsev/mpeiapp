package kekmech.ru.feature_app_settings.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent.News
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent.Wish
import java.util.*

typealias AppSettingsResult = Result<AppSettingsState, AppSettingsEffect, AppSettingsAction>

class AppSettingsReducer : BaseReducer<AppSettingsState, AppSettingsEvent, AppSettingsEffect, AppSettingsAction> {

    override fun reduce(
        event: AppSettingsEvent,
        state: AppSettingsState
    ): AppSettingsResult = when (event) {
        is Wish -> reduceWish(event, state)
        is News -> reduceNews(event, state)
    }

    private fun reduceNews(
        event: News,
        state: AppSettingsState
    ): AppSettingsResult = when (event) {
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
    ): AppSettingsResult = when (event) {
        is Wish.Init -> Result(
            state = state,
            action = AppSettingsAction.LoadAppSettings
        )
        is Wish.Action.SetDarkThemeEnabled -> Result(
            state = state,
            action = AppSettingsAction.SetDarkThemeEnabled(event.isEnabled),
            effect = AppSettingsEffect.RecreateActivity
                .takeIf { event.isEnabled != state.appSettings?.isDarkThemeEnabled }
        )
        is Wish.Action.SetChangeDayAfterChangeWeek -> Result(
            state = state,
            action = AppSettingsAction.SetChangeDayAfterChangeWeek(event.isEnabled)
        )
        is Wish.Action.SetAutoHideBottomSheet -> Result(
            state = state,
            action = AppSettingsAction.SetAutoHideBottomSheet(event.isEnabled)
        )
    }

}