package kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm

import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.lib_app_info.AppVersionName
import money.vivid.elmslie.core.store.ElmStore

internal class AppSettingsStoreFactory(
    private val actor: AppSettingsActor,
    private val isSnowFlakesEnabledFeatureToggle: IsSnowFlakesEnabledFeatureToggle,
    private val appVersionName: AppVersionName,
) {

    fun create() =
        ElmStore(
            initialState = AppSettingsState(
                isFeatureToggleSnowFlakesEnabled = isSnowFlakesEnabledFeatureToggle.value,
                appVersionName = appVersionName,
            ),
            reducer = AppSettingsReducer(),
            actor = actor,
            startEvent = AppSettingsEvent.Ui.Init,
        )
}
