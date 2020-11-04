package kekmech.ru.feature_app_settings.screens.main.presentation

import kekmech.ru.common_mvi.BaseFeature

internal class AppSettingsFeatureFactory(
    private val actor: AppSettingsActor
) {

    fun create() = BaseFeature(
        initialState = AppSettingsState(),
        reducer = AppSettingsReducer(),
        actor = actor
    ).start()
}