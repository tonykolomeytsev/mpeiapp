package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.common_feature_toggles.FeatureToggles
import vivid.money.elmslie.core.store.ElmStore

internal class AppSettingsFeatureFactory(
    private val actor: AppSettingsActor,
    private val featureToggles: FeatureToggles
) {

    fun create() = ElmStore(
        initialState = AppSettingsState(
            isFeatureToggleSnowFlakesEnabled = featureToggles.isSnowFlakesEnabled
        ),
        reducer = AppSettingsReducer(),
        actor = actor
    )
}