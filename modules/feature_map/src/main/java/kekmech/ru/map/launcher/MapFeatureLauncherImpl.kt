package kekmech.ru.map.launcher

import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.map.MapFragment

internal class MapFeatureLauncherImpl(
    private val deeplinkDelegate: DeeplinkDelegate
) : MapFeatureLauncher {

    override fun launchMain() = MapFragment()

    override fun selectPlace(placeUid: String) {
        deeplinkDelegate.putPlaceUid(placeUid)
    }

    override fun selectTab(tabName: String) {
        deeplinkDelegate.putTab(tabName)
    }
}