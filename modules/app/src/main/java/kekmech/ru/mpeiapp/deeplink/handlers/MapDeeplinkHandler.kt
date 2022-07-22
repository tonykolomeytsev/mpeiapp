package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.common_navigation.BottomTab
import kekmech.ru.common_navigation.BottomTabsSwitcher
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler
import kekmech.ru.mpeiapp.ui.main.MainFragment

class MapDeeplinkHandler(
    private val router: Router,
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val mapFeatureLauncher: MapFeatureLauncher
) : DeeplinkHandler("map") {

    override fun handle(deeplink: Uri) {
        val placeUid = deeplink.getQueryParameter("placeUid").orEmpty()
        val tab = deeplink.getQueryParameter("tab").orEmpty()
        router.executeCommand(PopUntil(MainFragment::class, inclusive = false))
        if (placeUid.isNotBlank()) {
            mapFeatureLauncher.selectPlace(placeUid)
        } else if (tab.isNotBlank()) {
            mapFeatureLauncher.selectTab(tab)
        }
        bottomTabsSwitcher.changeTab(BottomTab.MAP)
    }
}
