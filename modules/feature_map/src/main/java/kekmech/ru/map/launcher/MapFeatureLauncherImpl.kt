package kekmech.ru.map.launcher

import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.map.MapFragment

internal class MapFeatureLauncherImpl(
    private val selectedPlaceDelegate: SelectedPlaceDelegate
) : MapFeatureLauncher {

    override fun launchMain() = MapFragment()

    override fun selectPlace(placeUid: String) {
        selectedPlaceDelegate.put(placeUid)
    }
}