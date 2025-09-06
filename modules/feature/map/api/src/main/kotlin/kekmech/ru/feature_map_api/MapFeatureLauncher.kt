package kekmech.ru.feature_map_api

import androidx.fragment.app.Fragment

public interface MapFeatureLauncher {

    public fun launchMain(): Fragment

    public fun selectPlace(placeUid: String)

    public fun selectTab(tabName: String)
}
