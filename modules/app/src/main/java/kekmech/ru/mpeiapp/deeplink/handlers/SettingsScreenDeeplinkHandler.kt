package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler

class SettingsScreenDeeplinkHandler(
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val appSettingsFeatureLauncher: AppSettingsFeatureLauncher
) : DeeplinkHandler("settings") {

    override fun handle(deeplink: Uri) {
        bottomTabsSwitcher.changeTab(BottomTab.PROFILE)
        appSettingsFeatureLauncher.launch()
    }
}
