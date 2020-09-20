package kekmech.ru.feature_app_settings.presentation

import kekmech.ru.common_mvi.BaseReducer
import kekmech.ru.common_mvi.Result
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent.News
import kekmech.ru.feature_app_settings.presentation.AppSettingsEvent.Wish

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
        else -> Result(state = state)
    }

    private fun reduceWish(
        event: Wish,
        state: AppSettingsState
    ): AppSettingsResult = when (event) {
        else -> Result(state = state)
    }


}