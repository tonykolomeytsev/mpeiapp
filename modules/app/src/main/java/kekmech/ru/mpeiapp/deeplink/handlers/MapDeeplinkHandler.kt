package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.lib_navigation.BottomTab
import kekmech.ru.lib_navigation.BottomTabsSwitcher
import kekmech.ru.mpeiapp.deeplink.DeeplinkHandler

class MapDeeplinkHandler(
    private val bottomTabsSwitcher: BottomTabsSwitcher,
    private val mapFeatureLauncher: MapFeatureLauncher
) : DeeplinkHandler("map") {

    override fun handle(deeplink: Uri) {
        val placeUid = deeplink.getQueryParameter("placeUid").orEmpty()
        val tab = deeplink.getQueryParameter("tab").orEmpty()
        if (placeUid.isNotBlank()) {
            mapFeatureLauncher.selectPlace(placeUid)
        } else if (tab.isNotBlank()) {
            mapFeatureLauncher.selectTab(tab)
        }
        bottomTabsSwitcher.changeTab(BottomTab.MAP)
    }
}
