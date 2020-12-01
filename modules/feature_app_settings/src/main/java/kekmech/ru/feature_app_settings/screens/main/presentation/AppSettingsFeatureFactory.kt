package kekmech.ru.feature_app_settings.screens.main.presentation

import android.content.SharedPreferences
import kekmech.ru.common_feature_toggles.FeatureToggles
import kekmech.ru.common_mvi.BaseFeature

internal class AppSettingsFeatureFactory(
    private val actor: AppSettingsActor,
    private val featureToggles: FeatureToggles
) {

    fun create() = BaseFeature(
        initialState = AppSettingsState(
            isFeatureToggleSnowFlakesEnabled = featureToggles.isSnowFlakesEnabled
        ),
        reducer = AppSettingsReducer(),
        actor = actor
    ).start()
}