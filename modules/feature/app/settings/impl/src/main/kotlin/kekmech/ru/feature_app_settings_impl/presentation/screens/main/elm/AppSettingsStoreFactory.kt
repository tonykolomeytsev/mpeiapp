package kekmech.ru.feature_app_settings_impl.presentation.screens.main.elm

import kekmech.ru.feature_app_settings_api.IsSnowFlakesEnabledFeatureToggle
import kekmech.ru.library_app_info.AppVersionName
import vivid.money.elmslie.coroutines.ElmStoreCompat

internal class AppSettingsStoreFactory(
    private val actor: AppSettingsActor,
    private val isSnowFlakesEnabledFeatureToggle: IsSnowFlakesEnabledFeatureToggle,
    private val appVersionName: AppVersionName,
) {

    fun create() =
        ElmStoreCompat(
            initialState = AppSettingsState(
                isFeatureToggleSnowFlakesEnabled = isSnowFlakesEnabledFeatureToggle.value,
                appVersionName = appVersionName,
            ),
            reducer = AppSettingsReducer(),
            actor = actor,
            startEvent = AppSettingsEvent.Ui.Init,
        )
}
