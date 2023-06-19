package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.common_feature_toggles.FeatureToggles
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class AppSettingsStoreFactory(
    private val actor: AppSettingsActor,
    private val featureToggles: FeatureToggles,
) {

    fun create() =
        ElmStoreCompat(
            initialState = AppSettingsState(
                isFeatureToggleSnowFlakesEnabled = featureToggles.isSnowFlakesEnabled,
            ),
            reducer = AppSettingsReducer(),
            actor = actor,
            startEvent = AppSettingsEvent.Ui.Init,
        )
}
