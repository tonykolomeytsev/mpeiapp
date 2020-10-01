package kekmech.ru.map.presentation

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kekmech.ru.common_mvi.Feature
import kekmech.ru.domain_app_settings.AppSettings
import kekmech.ru.domain_map.dto.MapMarker

typealias MapFeature = Feature<MapState, MapEvent, MapEffect>

data class MapState(
    val selectedTab: FilterTab = FilterTab.FOOD,
    val markers: List<MapMarker> = emptyList(),
    val map: GoogleMap? = null,
    val bottomSheetState: Int = BottomSheetBehavior.STATE_COLLAPSED,
    val googleMapMarkers: List<Marker> = emptyList(),
    val appSettings: AppSettings
)

enum class FilterTab { FOOD, BUILDINGS, HOSTELS, OTHERS, STRUCTURES }

sealed class MapEvent {

    sealed class Wish : MapEvent() {
        object Init : Wish()

        object Action {
            data class OnMapReady(val map: GoogleMap) : Wish()
            data class SelectTab(val tab: FilterTab) : Wish()
            data class BottomSheetStateChanged(val newState: Int) : Wish()
            data class GoogleMapMarkersGenerated(val googleMapMarkers: List<Marker>) : Wish()
            data class OnListMarkerSelected(val mapMarker: MapMarker) : Wish()
        }
    }

    sealed class News : MapEvent() {
        data class MapMarkersLoaded(val markers: List<MapMarker>) : News()
        data class MapMarkersLoadError(val throwable: Throwable) : News()
    }
}

sealed class MapEffect {
    data class GenerateGoogleMapMarkers(
        val map: GoogleMap?,
        val markers: List<MapMarker>?,
        val googleMapMarkers: List<Marker>,
        val selectedTab: FilterTab
    ) : MapEffect()
    data class AnimateCameraToPlace(
        val map: GoogleMap,
        val googleMapMarkers: List<Marker>,
        val mapMarker: MapMarker
    ) : MapEffect()
}

sealed class MapAction {
    object ObserveMarkers : MapAction()
}