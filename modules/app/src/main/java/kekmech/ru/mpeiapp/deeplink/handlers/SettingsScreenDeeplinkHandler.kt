package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.feature_app_settings_api.AppSettingsFeatureLauncher
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.library_navigation.PopUntil
import kekmech.ru.library_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.ui.main.MainFragment

class SettingsScreenDeeplinkHandler(
    private val router: Router,
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val appSettingsFeatureLauncher: AppSettingsFeatureLauncher
) : DeeplinkHandler("settings") {

    override fun handle(deeplink: Uri) {
        router.executeCommand(PopUntil(MainFragment::class, inclusive = false))
        bottomTabsSwitcher.changeTab(BottomTab.PROFILE)
        appSettingsFeatureLauncher.launch()
    }
}
