package kekmech.ru.feature_app_settings.screens.main.elm

import kekmech.ru.domain_app_settings.feature_toggle.IsSnowFlakesEnabledFeatureToggle
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class AppSettingsStoreFactory(
    private val actor: AppSettingsActor,
    private val isSnowFlakesEnabledFeatureToggle: IsSnowFlakesEnabledFeatureToggle,
) {

    fun create() =
        ElmStoreCompat(
            initialState = AppSettingsState(
                isFeatureToggleSnowFlakesEnabled = isSnowFlakesEnabledFeatureToggle.value,
            ),
            reducer = AppSettingsReducer(),
            actor = actor,
            startEvent = AppSettingsEvent.Ui.Init,
        )
}
