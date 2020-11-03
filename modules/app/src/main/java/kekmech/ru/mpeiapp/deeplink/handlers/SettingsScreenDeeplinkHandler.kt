package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_app_settings.AppSettingsFeatureLauncher
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