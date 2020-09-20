package kekmech.ru.feature_app_settings.presentation

import kekmech.ru.common_mvi.BaseFeature

class AppSettingsFeatureFactory(
    private val actor: AppSettingsActor
) {

    fun create() = BaseFeature(
        initialState = AppSettingsState(),
        reducer = AppSettingsReducer(),
        actor = actor
    ).start()
}