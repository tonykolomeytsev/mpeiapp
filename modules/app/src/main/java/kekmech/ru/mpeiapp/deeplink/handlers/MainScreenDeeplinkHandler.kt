package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.lib_navigation.PopUntil
import kekmech.ru.lib_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.ui.main.MainFragment

class MainScreenDeeplinkHandler(
    private val router: Router,
    private val bottomTabsSwitcher: BottomTabsSwitcher
) : DeeplinkHandler("mainscreen") {

    override fun handle(deeplink: Uri) {
        router.executeCommand(PopUntil(MainFragment::class, inclusive = false))
        bottomTabsSwitcher.changeTab(BottomTab.DASHBOARD)
    }
}
