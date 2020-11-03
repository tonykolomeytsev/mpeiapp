package kekmech.ru.domain_map

import androidx.fragment.app.Fragment

interface MapFeatureLauncher {

    fun launchMain(): Fragment

    fun selectPlace(placeUid: String)
}