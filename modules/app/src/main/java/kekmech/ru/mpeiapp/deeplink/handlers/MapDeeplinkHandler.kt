package kekmech.ru.mpeiapp.deeplink.handlers

import android.net.Uri
import kekmech.ru.feature_map_api.MapFeatureLauncher
import kekmech.ru.library_navigation.BottomTab
import kekmech.ru.library_navigation.BottomTabsSwitcher
import kekmech.ru.library_navigation.PopUntil
import kekmech.ru.library_navigation.Router
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
