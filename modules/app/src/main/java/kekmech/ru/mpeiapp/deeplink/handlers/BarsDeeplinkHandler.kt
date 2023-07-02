package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.library_navigation.PopUntil
import kekmech.ru.library_navigation.Router
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.ui.main.MainFragment

class BarsDeeplinkHandler(
    private val router: Router,
    private val bottomTabsSwitcher: BottomTabsSwitcher
) : DeeplinkHandler("bars") {

    override fun handle(deeplink: Uri) {
        router.executeCommand(PopUntil(MainFragment::class, inclusive = false))
        bottomTabsSwitcher.changeTab(BottomTab.PROFILE)
    }
}
