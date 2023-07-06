package kekmech.ru.feature_map_api

import androidx.fragment.app.Fragment

interface MapFeatureLauncher {

    fun launchMain(): Fragment

    fun selectPlace(placeUid: String)

    fun selectTab(tabName: String)
}
