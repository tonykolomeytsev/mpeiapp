package kekmech.ru.map.presentation

import com.google.android.gms.maps.GoogleMap
import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_map.dto.MapMarker

typealias MapFeature = Feature<MapState, MapEvent, MapEffect>

data class MapState(
    val markers: List<MapMarker> = emptyList(),
    val map: GoogleMap? = null
)

sealed class MapEvent {

    sealed class Wish : MapEvent() {
        object Init : Wish()
    }

    sealed class News : MapEvent() {
        data class MapMarkersLoaded(val markers: List<MapMarker>) : News()
        data class MapMarkersLoadError(val throwable: Throwable) : News()
    }
}

sealed class MapEffect {

}

sealed class MapAction {
    object ObserveMarkers : MapAction()
}