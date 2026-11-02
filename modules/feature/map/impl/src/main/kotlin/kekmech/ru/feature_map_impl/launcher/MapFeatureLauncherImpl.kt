package kekmech.ru.feature_map_impl.launcher

import kekmech.ru.feature_map_api.MapFeatureLauncher

internal class MapFeatureLauncherImpl(
    private val deeplinkDelegate: DeeplinkDelegate
) : MapFeatureLauncher {

    override fun selectPlace(placeUid: String) {
        deeplinkDelegate.putPlaceUid(placeUid)
    }

    override fun selectTab(tabName: String) {
        deeplinkDelegate.putTab(tabName)
    }
}
