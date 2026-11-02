package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler

class MainScreenDeeplinkHandler(
    private val bottomTabsSwitcher: BottomTabsSwitcher
) : DeeplinkHandler("mainscreen") {

    override fun handle(deeplink: Uri) {
        bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
    }
}
