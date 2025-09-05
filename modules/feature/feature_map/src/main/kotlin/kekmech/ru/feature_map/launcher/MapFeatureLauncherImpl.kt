package kekmech.ru.feature_map.launcher

import androidx.fragment.app.Fragment
import kekmech.ru.domain_map.MapFeatureLauncher
import kekmech.ru.feature_map.screens.main.MapFragment

internal class MapFeatureLauncherImpl(
    private val deeplinkDelegate: DeeplinkDelegate
) : MapFeatureLauncher {

    override fun launchMain(): Fragment = MapFragment()

    override fun selectPlace(placeUid: String) {
        deeplinkDelegate.putPlaceUid(placeUid)
    }

    override fun selectTab(tabName: String) {
        deeplinkDelegate.putTab(tabName)
    }
}
